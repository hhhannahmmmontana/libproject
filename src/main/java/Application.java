import com.volodya.application.configuration.AppConfig;
import com.volodya.application.configuration.PersistenceConfig;
import com.volodya.application.configuration.PropertiesConfig;
import com.volodya.application.configuration.ValidationConfig;
import com.volodya.application.configurationproperties.ServerConfigurationProperties;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

void main() throws LifecycleException {
    var propsContext = new AnnotationConfigApplicationContext(PropertiesConfig.class);
    var properties = propsContext.getBean(ServerConfigurationProperties.class);
    propsContext.close();

    var tomcat = new Tomcat();
    tomcat.setPort(properties.port());
    tomcat.getConnector();
    var tomcatContext = tomcat.addContext("", null);
    var host = (StandardHost) tomcat.getHost();
    host.setErrorReportValveClass(null);

    var springContext = new AnnotationConfigWebApplicationContext();
    springContext.register(
        AppConfig.class,
        PropertiesConfig.class,
        PersistenceConfig.class,
        ValidationConfig.class
    );
    var dispatcherServlet = new DispatcherServlet(springContext);

    var servletWrapper = Tomcat.addServlet(tomcatContext, "dispatcherServlet", dispatcherServlet);
    servletWrapper.addMapping("/");
    servletWrapper.setLoadOnStartup(0);
    tomcat.start();
}