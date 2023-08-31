package org.walley.wteplota;

public class wt_device
{
  private String name;
  private String value;
  private String type;
  private String note;

  public wt_device(String name, String value)
  {
    this.setName(name);
    this.setValue(value);
  }

  public wt_device(String name, String value, String type)
  {
    this.setName(name);
    this.setValue(value);
    this.setType(type);
  }

  public wt_device(String name, String value, String type, String note)
  {
    this.setName(name);
    this.setValue(value);
    this.setType(type);
    this.setNote(note);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}

