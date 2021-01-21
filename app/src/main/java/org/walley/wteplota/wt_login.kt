package org.walley.wteplota

import android.annotation.TargetApi
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AnyThread
import androidx.annotation.ColorRes
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.snackbar.Snackbar
import net.openid.appauth.*
import net.openid.appauth.AuthorizationServiceConfiguration.RetrieveConfigurationCallback
import net.openid.appauth.browser.AnyBrowserMatcher
import net.openid.appauth.browser.BrowserDescriptor
import net.openid.appauth.browser.BrowserMatcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

//class wt_f_login : Fragment() {}


/*
 * Copyright 2015 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Demonstrates the usage of the AppAuth to authorize a user with an OAuth2 / OpenID Connect
 * provider. Based on the configuration provided in `res/raw/auth_config.json`, the code
 * contained here will:
 *
 * - Retrieve an OpenID Connect discovery document for the provider, or use a local static
 * configuration.
 * - Utilize dynamic client registration, if no static client id is specified.
 * - Initiate the authorization request using the built-in heuristics or a user-selected browser.
 *
 * _NOTE_: From a clean checkout of this project, the authorization service is not configured.
 * Edit `res/values/auth_config.xml` to provide the required configuration properties. See the
 * README.md in the app/ directory for configuration instructions, and the adjacent IDP-specific
 * instructions.
 */

class wt_login : AppCompatActivity() {

  internal class BrowserInfo(val mDescriptor: BrowserDescriptor, val mLabel: CharSequence, val mIcon: Drawable)

  private var mAuthService: AuthorizationService? = null
  private var mAuthStateManager: AuthStateManager? = null
  private var mConfiguration: Configuration? = null
  private val mClientId = AtomicReference<String>()
  private val mAuthRequest = AtomicReference<AuthorizationRequest?>()
  private val mAuthIntent = AtomicReference<CustomTabsIntent?>()
  private var mAuthIntentLatch = CountDownLatch(1)
  private var mExecutor: ExecutorService? = null
  private var mUsePendingIntents = false
  private var mBrowserMatcher: BrowserMatcher = AnyBrowserMatcher.INSTANCE
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    mExecutor = Executors.newSingleThreadExecutor()
    mAuthStateManager = AuthStateManager.getInstance(this)
    mConfiguration = Configuration.getInstance(this)

    if (mAuthStateManager == null) {
      Log.i(TAG, "mAuthStateManager is null")
    } else {
      Log.i(TAG, "mAuthStateManager is NOT null")
    }

    if (mAuthStateManager?.getCurrent()!!.isAuthorized() && !mConfiguration?.hasConfigurationChanged()!!) {
      Log.i(TAG, "User is already authenticated, proceeding to token activity")
      startActivity(Intent(this, TokenActivity::class.java))
      finish()
      return
    }

    Log.i(TAG, "1")

    setContentView(R.layout.activity_login)
    findViewById<View>(R.id.retry).setOnClickListener { view: View? -> mExecutor?.submit(Runnable { initializeAppAuth() }) }
    findViewById<View>(R.id.start_auth).setOnClickListener { view: View? -> startAuth() }
    (findViewById<View>(R.id.login_hint_value) as EditText).addTextChangedListener(LoginHintChangeHandler())
    if (!mConfiguration?.isValid()!!) {
      displayError(mConfiguration?.getConfigurationError()!!, false)
      return
    }
    Log.i(TAG, "2")

    configureBrowserSelector()

    Log.i(TAG, "3")

    if (mConfiguration?.hasConfigurationChanged()!!) {
      // discard any existing authorization state due to the change of configuration
      Log.i(TAG, "Configuration change detected, discarding old state")
      mAuthStateManager?.replace(AuthState())
      mConfiguration?.acceptConfiguration()!!
    }

    if (intent.getBooleanExtra(EXTRA_FAILED, false)) {
      displayAuthCancelled()
    }
    displayLoading("Initializing")
    mExecutor?.submit(Runnable { initializeAppAuth() })
  }

  override fun onStart() {
    super.onStart()
    if (mExecutor!!.isShutdown) {
      mExecutor = Executors.newSingleThreadExecutor()
    }
  }

  override fun onStop() {
    super.onStop()
    mExecutor!!.shutdownNow()
  }

  override fun onDestroy() {
    super.onDestroy()
    if (mAuthService != null) {
      mAuthService!!.dispose()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    displayAuthOptions()
    if (resultCode == Activity.RESULT_CANCELED) {
      displayAuthCancelled()
    } else {
      val intent = Intent(this, TokenActivity::class.java)
      intent.putExtras(data!!.extras!!)
      startActivity(intent)
    }
  }

  @MainThread
  fun startAuth() {
    displayLoading("Making authorization request")
    Toast.makeText(applicationContext, "x", Toast.LENGTH_LONG).show()

//    mUsePendingIntents = (findViewById<View>(R.id.pending_intents_checkbox) as CheckBox).isChecked
    mUsePendingIntents = false;
    mExecutor!!.submit { doAuth() }
  }

  /**
   * Initializes the authorization service configuration if necessary, either from the local
   * static values or by retrieving an OpenID discovery document.
   */
  @WorkerThread
  private fun initializeAppAuth() {
    Log.i(TAG, "Initializing AppAuth")
    recreateAuthorizationService()
    if (mAuthStateManager?.getCurrent()!!.getAuthorizationServiceConfiguration()!! != null) {
      // configuration is already created, skip to client initialization
      Log.i(TAG, "auth config already established")
      initializeClient()
      return
    }

    // if we are not using discovery, build the authorization service configuration directly
    // from the static configuration values.
    if (mConfiguration?.getDiscoveryUri() == null) {
      Log.i(TAG, "Creating auth config from res/raw/auth_config.json")
      val config = AuthorizationServiceConfiguration(mConfiguration?.getAuthEndpointUri()!!, mConfiguration?.getTokenEndpointUri()!!, mConfiguration?.getRegistrationEndpointUri()!!)
      mAuthStateManager?.replace(AuthState(config))
      initializeClient()
      return
    }

    // WrongThread inference is incorrect for lambdas
    // noinspection WrongThread
    runOnUiThread { displayLoading("Retrieving discovery document") }
    Log.i(TAG, "Retrieving OpenID discovery doc")
    AuthorizationServiceConfiguration.fetchFromUrl(mConfiguration?.getDiscoveryUri()!!, RetrieveConfigurationCallback { config: AuthorizationServiceConfiguration?, ex: AuthorizationException? -> handleConfigurationRetrievalResult(config, ex) }, mConfiguration?.getConnectionBuilder()!!)
  }

  @MainThread
  private fun handleConfigurationRetrievalResult(config: AuthorizationServiceConfiguration?, ex: AuthorizationException?) {
    if (config == null) {
      Log.i(TAG, "Failed to retrieve discovery document", ex)
      displayError("Failed to retrieve discovery document: " + ex!!.message, true)
      return
    }
    Log.i(TAG, "Discovery document retrieved")
    mAuthStateManager?.replace(AuthState(config))
    mExecutor!!.submit { initializeClient() }
  }

  /**
   * Initiates a dynamic registration request if a client ID is not provided by the static
   * configuration.
   */
  @WorkerThread
  private fun initializeClient() {
    if (mConfiguration?.getClientId() != null) {
//      Log.i(TAG, "Using static client ID: " + (mConfiguration?.getClientId() ?: ))
      // use a statically configured client ID
      mClientId.set(mConfiguration!!.getClientId())
      runOnUiThread { initializeAuthRequest() }
      return
    }
    val lastResponse: RegistrationResponse = mAuthStateManager?.getCurrent()?.getLastRegistrationResponse()!!
    if (lastResponse != null) {
      Log.i(TAG, "Using dynamic client ID: " + lastResponse.clientId)
      // already dynamically registered a client ID
      mClientId.set(lastResponse.clientId)
      runOnUiThread { initializeAuthRequest() }
      return
    }

    // WrongThread inference is incorrect for lambdas
    // noinspection WrongThread
    //runOnUiThread { displayLoading("Dynamically registering client") }
    //Log.i(TAG, "Dynamically registering client")
    //val registrationRequest = RegistrationRequest.Builder(mAuthStateManager?.getCurrent().getAuthorizationServiceConfiguration(), listOf(mConfiguration.getRedirectUri())).setTokenEndpointAuthenticationMethod(ClientSecretBasic.NAME).build()
    //mAuthService!!.performRegistrationRequest(registrationRequest) { response: RegistrationResponse?, ex: AuthorizationException? -> handleRegistrationResponse(response, ex) }
  }

  @MainThread
  private fun handleRegistrationResponse(response: RegistrationResponse?, ex: AuthorizationException?) {
    mAuthStateManager?.updateAfterRegistration(response, ex)
    if (response == null) {
      Log.i(TAG, "Failed to dynamically register client", ex)
      displayErrorLater("Failed to register client: " + ex!!.message, true)
      return
    }
    Log.i(TAG, "Dynamically registered client: " + response.clientId)
    mClientId.set(response.clientId)
    initializeAuthRequest()
  }

  /**
   * Enumerates the browsers installed on the device and populates a spinner, allowing the
   * demo user to easily test the authorization flow against different browser and custom
   * tab configurations.
   */
  @MainThread
  private fun configureBrowserSelector() {

    Log.i(TAG, "1")

    mBrowserMatcher = AnyBrowserMatcher.INSTANCE
    Log.i(TAG, "2")
    recreateAuthorizationService()
    Log.i(TAG, "3")
    createAuthRequest("x")
    Log.i(TAG, "4")
    warmUpBrowser()
    Log.i(TAG, "5")

/*
    val spinner = findViewById<View>(R.id.browser_selector) as Spinner
    val adapter = BrowserSelectionAdapter(this)
    spinner.adapter = adapter
    spinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val info: BrowserInfo = adapter.getItem(position)
        if (info == null) {
          mBrowserMatcher = AnyBrowserMatcher.INSTANCE
          return
        } else {
          mBrowserMatcher = ExactBrowserMatcher(info.mDescriptor)
        }
        recreateAuthorizationService()
        createAuthRequest(loginHint)
        warmUpBrowser()
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
        mBrowserMatcher = AnyBrowserMatcher.INSTANCE
      }
    }

 */
  }

  /**
   * Performs the authorization request, using the browser selected in the spinner,
   * and a user-provided `login_hint` if available.
   */
  @WorkerThread
  private fun doAuth() {
    Log.w(TAG, "do_auth")
    try {
      mAuthIntentLatch.await()
    } catch (ex: InterruptedException) {
      Log.w(TAG, "Interrupted while waiting for auth intent")
    }

    Log.w(TAG, "do_auth2")

    if (mUsePendingIntents) {
      Log.w(TAG, "Intent wind data")
      val completionIntent = Intent(this, TokenActivity::class.java)
      val cancelIntent = Intent(this, wt_login::class.java)
      cancelIntent.putExtra(EXTRA_FAILED, true)
      cancelIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      mAuthService!!.performAuthorizationRequest(mAuthRequest.get()!!, PendingIntent.getActivity(this, 0, completionIntent, 0), PendingIntent.getActivity(this, 0, cancelIntent, 0), mAuthIntent.get()!!)
    } else {
      Log.w(TAG, "Intent")
      val intent = mAuthService!!.getAuthorizationRequestIntent(mAuthRequest.get()!!, mAuthIntent.get()!!)
      startActivityForResult(intent, RC_AUTH)
    }
  }

  private fun recreateAuthorizationService() {
    if (mAuthService != null) {
      Log.i(TAG, "Discarding existing AuthService instance")
      mAuthService!!.dispose()
    }
    mAuthService = createAuthorizationService()
    mAuthRequest.set(null)
    mAuthIntent.set(null)
  }

  private fun createAuthorizationService(): AuthorizationService {
    Log.i(TAG, "Creating authorization service")
    val builder = AppAuthConfiguration.Builder()
    builder.setBrowserMatcher(mBrowserMatcher)
    builder.setConnectionBuilder(mConfiguration?.getConnectionBuilder()!!)
    return AuthorizationService(this, builder.build())
  }

  @MainThread
  private fun displayLoading(loadingMessage: String) {
//    findViewById<View>(R.id.loading_container).visibility = View.VISIBLE
//    findViewById<View>(R.id.auth_container).visibility = View.GONE
//    findViewById<View>(R.id.error_container).visibility = View.GONE
//    (findViewById<View>(R.id.loading_description) as TextView).text = loadingMessage
  }

  @MainThread
  private fun displayError(error: String, recoverable: Boolean) {
//    findViewById<View>(R.id.error_container).visibility = View.VISIBLE
//    findViewById<View>(R.id.loading_container).visibility = View.GONE
//    findViewById<View>(R.id.auth_container).visibility = View.GONE
//    (findViewById<View>(R.id.error_description) as TextView).text = error
//    findViewById<View>(R.id.retry).visibility = if (recoverable) View.VISIBLE else View.GONE
  }

  // WrongThread inference is incorrect in this case
  @AnyThread
  private fun displayErrorLater(error: String, recoverable: Boolean) {
    runOnUiThread { displayError(error, recoverable) }
  }

  @MainThread
  private fun initializeAuthRequest() {
    createAuthRequest(loginHint)
    warmUpBrowser()
    displayAuthOptions()
  }

  @MainThread
  private fun displayAuthOptions() {
//    findViewById<View>(R.id.auth_container).visibility = View.VISIBLE
//    findViewById<View>(R.id.loading_container).visibility = View.GONE
//    findViewById<View>(R.id.error_container).visibility = View.GONE
    val state: AuthState = mAuthStateManager?.getCurrent()!!
    val config = state.authorizationServiceConfiguration
    var authEndpointStr: String
    authEndpointStr = if (config!!.discoveryDoc != null) {
      "Discovered auth endpoint: \n"
    } else {
      "Static auth endpoint: \n"
    }
    authEndpointStr += config.authorizationEndpoint
    (findViewById<View>(R.id.auth_endpoint) as TextView).text = authEndpointStr
    var clientIdStr: String
    clientIdStr = if (state.lastRegistrationResponse != null) {
      "Dynamic client ID: \n"
    } else {
      "Static client ID: \n"
    }
    clientIdStr += mClientId
    (findViewById<View>(R.id.client_id) as TextView).text = clientIdStr
  }

  private fun displayAuthCancelled() {
    Snackbar.make(findViewById(R.id.coordinator), "Authorization canceled", Snackbar.LENGTH_SHORT).show()
  }

  private fun warmUpBrowser() {
    mAuthIntentLatch = CountDownLatch(1)
    mExecutor!!.execute {
      Log.i(TAG, "Warming up browser instance for auth request")
      val intentBuilder = mAuthService!!.createCustomTabsIntentBuilder(mAuthRequest.get()!!.toUri())
      intentBuilder.setToolbarColor(getColorCompat(R.color.colorPrimary))
      mAuthIntent.set(intentBuilder.build())
      mAuthIntentLatch.countDown()
    }
  }

  private fun createAuthRequest(loginHint: String?) {
    Log.i(TAG, "Creating auth request for login hint: $loginHint")
    val x = mAuthStateManager?.getCurrent()?.getAuthorizationServiceConfiguration()

    if (x == null) {
      Log.i(TAG, "x is null")

    }

    val uri = mConfiguration?.getRedirectUri()
    Log.i(TAG, "uri: $uri")

    val scope = mConfiguration?.getScope()
    Log.i(TAG, "sco: $scope")

    var client_id = mClientId.get()
    client_id = "r1t5q2R87A0t3kYaQ0uSAPdnO5DsK1J8qTbGQ3xLlPFniI2j8Savpo3vyElQpzEk"
    Log.i(TAG, "cli: $client_id")

    val authRequestBuilder = AuthorizationRequest.Builder(x!!, client_id, ResponseTypeValues.CODE, uri!!).setScope(scope)

    Log.i(TAG, "YO")

    if (!TextUtils.isEmpty(loginHint)) {
      authRequestBuilder.setLoginHint(loginHint)
    }
    mAuthRequest.set(authRequestBuilder.build())
    Log.i(TAG, "YOYI")
  }

  private val loginHint: String
    private get() = (findViewById<View>(R.id.login_hint_value) as EditText).text.toString().trim { it <= ' ' }

  @TargetApi(Build.VERSION_CODES.M)
  private fun getColorCompat(@ColorRes color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getColor(color)
    } else {
      resources.getColor(color)
    }
  }

  /**
   * Responds to changes in the login hint. After a "debounce" delay, warms up the browser
   * for a request with the new login hint; this avoids constantly re-initializing the
   * browser while the user is typing.
   */
  private inner class LoginHintChangeHandler internal constructor() : TextWatcher {
    private val mHandler: Handler
    private var mTask: org.walley.wteplota.wt_login.RecreateAuthRequestTask
    override fun beforeTextChanged(cs: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(cs: CharSequence, start: Int, before: Int, count: Int) {
      mTask.cancel()
      mTask = RecreateAuthRequestTask()
      mHandler.postDelayed(mTask, DEBOUNCE_DELAY_MS.toLong())
    }

    override fun afterTextChanged(ed: Editable) {}

    val DEBOUNCE_DELAY_MS = 500

    init {
      mHandler = Handler(Looper.getMainLooper())
      mTask = RecreateAuthRequestTask()
    }
  }

  private inner class RecreateAuthRequestTask : Runnable {
    private val mCanceled = AtomicBoolean()
    override fun run() {
      if (mCanceled.get()) {
        return
      }
      createAuthRequest(loginHint)
      warmUpBrowser()
    }

    fun cancel() {
      mCanceled.set(true)
    }
  }

  companion object {
    private const val TAG = "LoginActivity"
    private const val EXTRA_FAILED = "failed"
    private const val RC_AUTH = 100
  }
}