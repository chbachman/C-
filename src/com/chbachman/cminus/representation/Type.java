package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chandler on 4/12/17.
 */
public class Type implements Typed {
    private static Map<String, Type> types = new HashMap<>();

    public static void init() {
        for (Native n : Native.values()) {
            types.put(n.type.type, n.type);
        }
    }

    public final String type;
    public final String cType;

    private Type(String type) {
        this(type, type);
    }

    private Type(String type, String cType) {
        this.type = type;
        this.cType = cType;
    }

    public String code() {
        return cType;
    }

    @Override
    public Type type() {
        return this;
    }

    public static Type from(CMinusParser.TypeContext context) {
        String name = context.ID().getText();

        if (types.containsKey(name)) {
            return types.get(name);
        } else {
            Type t = new Type(name);
            types.put(name, t);
            return t;
        }
    }

    public enum Native {

        STRING("String", "char *"),
        INT("Int", "int"),
        VOID("Void", "void");

        public Type type;

        Native(String type) {
            this(type, type);
        }

        Native(String type, String cType) {
            this.type = new Type(type, cType);
        }

        public static Native get(Type type) {
            for (Native n : Native.values()) {
                if (n.type == type) {
                    return n;
                }
            }

            return null;
        }

    }
}
