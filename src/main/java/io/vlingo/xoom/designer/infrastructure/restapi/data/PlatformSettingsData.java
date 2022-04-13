package io.vlingo.xoom.designer.infrastructure.restapi.data;

public class PlatformSettingsData {
public final String platform;
public final String lang;
public final String sdkVersion;

  public PlatformSettingsData(String platform, String lang, String sdkVersion) {
    this.platform = platform;
    this.lang = lang;
    this.sdkVersion = sdkVersion;
  }
}
