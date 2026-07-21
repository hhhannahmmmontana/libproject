import com.volodya.AppConfig;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

void main() throws LifecycleException {
    var tomcat = new Tomcat();
    tomcat.setPort(8080);
    tomcat.getConnector();
    var tomcatContext = tomcat.addContext("", null);

    var springContext = new AnnotationConfigWebApplicationContext();
    springContext.register(AppConfig.class);
    var dispatcherServlet = new DispatcherServlet(springContext);

    var servletWrapper = Tomcat.addServlet(tomcatContext, "dispatcherServlet", dispatcherServlet);
    servletWrapper.addMapping("/");
    tomcat.start();
}