package extension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import message.HTTPMessage;

public class ExtensionService {
    
    public List<Class<?>> services;

    public ExtensionService(){
        this.services = new ArrayList<>();
        
        this.services.add(LoggingService.class);
    }

    public void verifyBefore(Object informationContext){
        try {
            if(services.size() > 0){
                for (Class<?> service : services) {
                    for (Method method : service.getMethods()) {
                        if(method.getName().equals("verifyBefore")){
                            method.invoke(service.getConstructor().newInstance(), informationContext);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyAfter(HTTPMessage informationContext){
        try {
            if(services.size() > 0){
                for (Class<?> service : services) {
                    for (Method method : service.getMethods()) {
                        if(method.getName().equals("verifyAfter")){
                            method.invoke(service.getConstructor().newInstance(), informationContext);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
