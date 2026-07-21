import com.volodya.AppConfig;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

void main() throws LifecycleException {
    var springContext = new AnnotationConfigWebApplicationContext();
    springContext.register(AppConfig.class);
    var dispatcherServlet = new DispatcherServlet(springContext);
    springContext.refresh();

    var env = springContext.getEnvironment();
    var port = env.getProperty("server.port", Integer.class);
    assert port != null;

    var tomcat = new Tomcat();
    tomcat.setPort(port);
    tomcat.getConnector();
    var tomcatContext = tomcat.addContext("", null);

    var servletWrapper = Tomcat.addServlet(tomcatContext, "dispatcherServlet", dispatcherServlet);
    servletWrapper.addMapping("/");
    tomcat.start();
}