package com.raczy.utility;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by kacperraczy on 30.12.2017.
 */

public class AnnotatedDeserializer<T> implements JsonDeserializer<T> {

    private static Logger log = LogManager.getLogger(AnnotatedDeserializer.class);

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        T pojo = new Gson().fromJson(jsonElement, type);

        Field[] fields = pojo.getClass().getDeclaredFields();
        for (Field f : fields)
        {
            if (f.getAnnotation(JsonRequired.class) != null)
            {
                try
                {
                    f.setAccessible(true);
                    if (f.get(pojo) == null)
                    {
                        return null;
                    }
                }
                catch (Exception ex)
                {
                    log.error(ex.getMessage());
                }
            }
        }
        return pojo;
    }
}

