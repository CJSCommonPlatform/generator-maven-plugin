package shiv;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.Unmanaged;

import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class EventSourceProvider {

    @Produces
    @EventSourceName
    public EventSource createInterface(InjectionPoint injectionPoint) {

        Annotation[] annotations1=injectionPoint.getBean().getBeanClass().getAnnotations();
        for (int i =0;i< annotations1.length;i++){
            System.out.println(annotations1[i].toString());
        }
        //System.out.println("Created createInterface called");
        Set<Annotation> annotations = injectionPoint.getQualifiers();

        Optional<Annotation> exists = annotations.stream().filter(a -> a instanceof EventSourceName).findFirst();
        String eventSourceName;

        if (exists.isPresent()) {
            EventSourceName eventSourceQualifier = (EventSourceName) exists.get();
            eventSourceName = eventSourceQualifier.value();
            if (StringUtils.isEmpty(eventSourceName)) {
                eventSourceName = "unknown";
            }
        } else {
            eventSourceName = "unknown";
        }
        //System.out.println("called with qualifier "+eventSourceName);
        return createAndInjectDependecny(eventSourceName);
    }

    @Produces
    @Default
    public EventSource createDefault() {
        System.out.println("Created default called");
        return createAndInjectDependecny("default");
    }

    @Produces
    @Default
    public Child createChild() {
        //System.out.println("Created child  called");
        return new Child("ssd");
    }


    private EventSource createAndInjectDependecny(String name) {
        final BeanManager beanManager = CDI.current().getBeanManager();
        if (isLocalAvailable()) {
            return local(name, beanManager);
        } else {
            return remote(name, beanManager);
        }
    }

    private boolean isLocalAvailable() {
        if (Math.random() < 0.5) {
            return true;
        } else {
            return false;
        }
    }

    private EventSource local(String name, BeanManager beanManager) {
        final Unmanaged<DefaultEventSource> unmanaged = new Unmanaged(beanManager, DefaultEventSource.class);
        final Unmanaged.UnmanagedInstance<DefaultEventSource> inst = unmanaged.newInstance();
        final DefaultEventSource defaultEventSource = inst.produce().inject().get();
        final Bean<DefaultEventSource> bean = (Bean<DefaultEventSource>) beanManager.resolve(beanManager.getBeans(DefaultEventSource.class));
        defaultEventSource.name = name;
        return defaultEventSource;
    }

    private EventSource remote(String name, BeanManager beanManager) {
        final Unmanaged<RemoteEventSource> unmanaged = new Unmanaged(beanManager, RemoteEventSource.class);
        final Unmanaged.UnmanagedInstance<RemoteEventSource> inst = unmanaged.newInstance();
        final RemoteEventSource remoteEventSource = inst.produce().inject().get();
        final Bean<RemoteEventSource> bean = (Bean<RemoteEventSource>) beanManager.resolve(beanManager.getBeans(RemoteEventSource.class));
        remoteEventSource.name = name;
        return remoteEventSource;
    }
}
