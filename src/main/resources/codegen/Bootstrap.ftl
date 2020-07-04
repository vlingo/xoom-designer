package ${packageName};

<#list imports as import>
import ${import.qualifiedClassName};
</#list>

import io.vlingo.actors.Stage;
import io.vlingo.xoom.actors.StageInitializationAware;
import io.vlingo.xoom.annotation.initializer.AddressFactory;
import io.vlingo.xoom.annotation.initializer.Xoom;

import static io.vlingo.xoom.annotation.initializer.AddressFactory.Type.UUID;

@Xoom(name = "${appName}", addressFactory = @AddressFactory(type = UUID))
<#if restResource.exist>
@Resources({${restResource.classNames}})
</#if>
public class Bootstrap implements StageInitializationAware {

   @Override
   public void onInit(final Stage stage) {
     <#list registries as registry>
         final ${registry.className} ${registry.objectName} = new ${registry.className}(stage.world());
     </#list>

     <#list providers as provider>
         ${provider.initialization}.using(${provider.arguments});
     </#list>
   }

}
