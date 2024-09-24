package com.xfheaderpoc.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Model(adaptables = {SlingHttpServletRequest.class},
  adapters = {ComponentExporter.class},
  resourceType = Header.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
  @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
  @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")})
@JsonSerialize(as = Header.class)
public class Header extends AbstractComponentImpl implements ComponentExporter {
  static final String RESOURCE_TYPE = "xfheaderpoc/components/header";
  private static final String ACCOUNT_MENU_ITEMS = "accountMenuItems";
  private static final String NAVIGATION_ITEMS = "navigationItems";
  @Getter
  private final List<Map<String, Object>> accountMenuMap = new ArrayList<>();
  @Getter
  private final List<Map<String, Object>> navigationMap = new ArrayList<>();
  @Getter
  @ValueMapValue
  protected String alt;
  @Getter
  @ValueMapValue
  @Default(booleanValues = false)
  protected boolean disableLanguageSelector;
  @Getter
  @ValueMapValue
  @Default(booleanValues = false)
  protected boolean disableMyAccountMenu;
  @Getter
  @ValueMapValue
  @Default(booleanValues = false)
  protected boolean disableSearch;
  @Getter
  @ValueMapValue
  protected String fileReference;
  @Getter
  protected String flyoutInsetInlineEnd;
  @Getter
  @ValueMapValue
  protected String linkTarget;
  @Getter
  @ValueMapValue
  protected String linkURL;

  @Nonnull
  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }

  @PostConstruct
  public void activate() {
    if (!disableLanguageSelector && !disableSearch) {
      flyoutInsetInlineEnd = "100px";
    } else if (disableLanguageSelector && disableSearch) {
      flyoutInsetInlineEnd = "0";
    } else {
      flyoutInsetInlineEnd = "50px";
    }


    Resource navigationChildResource = resource.getChild(NAVIGATION_ITEMS);

    if (null != navigationChildResource && navigationChildResource.hasChildren()) {
      for (Resource navigationResource : navigationChildResource.getChildren()) {
        ValueMap navigationProps = navigationResource.getValueMap();
        HashMap<String, Object> navigationPropsMap = new HashMap<>();
        copyProps(navigationProps, navigationPropsMap);
        navigationMap.add(navigationPropsMap);
      }
    }

    Resource accountChildResource = resource.getChild(ACCOUNT_MENU_ITEMS);

    if (null != accountChildResource && accountChildResource.hasChildren()) {
      for (Resource accountResource : accountChildResource.getChildren()) {
        ValueMap accountProps = accountResource.getValueMap();
        HashMap<String, Object> accountPropsMap = new HashMap<>();
        copyProps(accountProps, accountPropsMap);
        accountMenuMap.add(accountPropsMap);
      }
    }

  }

  private void copyProps(ValueMap from, HashMap<String, Object> to) {
    from.forEach((key, value) -> {
      if (!key.startsWith("jcr:") && !key.startsWith("sling:")) {
        to.put(key, value);
      }
    });
  }
}
