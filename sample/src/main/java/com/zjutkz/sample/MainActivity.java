package com.zjutkz.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zjutkz.dexerlib.Dexer;
import com.zjutkz.dexerlib.dex.Annotation;
import com.zjutkz.dexerlib.dex.AnnotationElement;
import com.zjutkz.dexerlib.dex.Class;
import com.zjutkz.dexerlib.dex.Field;
import com.zjutkz.dexerlib.dex.Method;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Sample";

    private Dexer dexer;

    @TestAnnotation(2)
    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void decode_dex(View view){
        try {
            byte[] src = stream2byte(getAssets().open("classes.dex"));
            dexer = new Dexer(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void get_all_classes(View view){
        dexer.getAllClasses(new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                List<Class> allClasses = (List<Class>)data;
                for(Class clz : allClasses){
                    Log.d(TAG, clz.source_file_name);
                }
            }
        });
    }

    public void get_all_methods(View view){
        dexer.getAllMethods(new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                List<Method> allMethods = (List<Method>)data;
                for(Method method : allMethods){
                    Log.d(TAG, method.name);
                }
            }
        });
    }

    public void get_all_methods_in_class(View view){
        dexer.getAllMethodsInClass("MainActivity",new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                List<Method> allMethods = (List<Method>)data;
                for(Method method : allMethods){
                    Log.d(TAG, method.name);
                }
            }
        });
    }

    public void has_method_and_class(View view){
        dexer.hasClass("MainActivity", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Log.d(TAG, "dex file has MainActivity: " + (boolean)data);
            }
        });

        dexer.hasClass("MainActivity2", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Log.d(TAG, "dex file has MainActivity2: " + (boolean)data);
            }
        });

        dexer.hasMethods("MainActivity", "get_all_methods_in_class", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Log.d(TAG, "MainActivity has method get_all_methods_in_class: " + (boolean)data);
            }
        });

        dexer.hasMethods("MainActivity", "get_all_methods_in_class2", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Log.d(TAG, "MainActivity has method get_all_methods_in_class2: " + (boolean)data);
            }
        });
    }

    public void get_class(View view){
        dexer.getClass("TestClass", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Class clz = (Class)data;
                Log.d(TAG, clz.class_name + " " + clz.super_class_name);
                for(Field field : clz.instance_fields){
                    Log.d(TAG, "instance field: " + field.name + " " + field.access_flag);
                }
                for(Field field : clz.static_fields){
                    Log.d(TAG, "static field: " + field.name + " " + field.access_flag);
                }
                Log.d(TAG, "" + clz.access_flags);
                Log.d(TAG, "=========================================");
            }
        });

        dexer.getClass("TestClass$InnerClass", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Class clz = (Class)data;
                for(Field field : clz.instance_fields){
                    Log.d(TAG, "instance field: " + field.name);
                }
                for(Field field : clz.static_fields){
                    Log.d(TAG, "static field: " + field.name);
                }
                Log.d(TAG, "" + clz.access_flags);
                Log.d(TAG, "=========================================");
            }
        });

        dexer.getClass("TestClass$InnerClass$DoubleInnerClass", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Class clz = (Class)data;
                for(Field field : clz.instance_fields){
                    Log.d(TAG, "instance field: " + field.name);
                }
                for(Field field : clz.static_fields){
                    Log.d(TAG, "static field: " + field.name);
                }
                Log.d(TAG, "" + clz.access_flags);
                Log.d(TAG, "=========================================");
            }
        });
    }

    public void get_method(View view){
        dexer.getMethod("MainActivity", "onCreate", new Dexer.OnFetchDataListener() {
            @Override
            public void onFetchDataSuccess(Object data) {
                Method method = (Method)data;
                Log.d(TAG, method.name);
                Log.d(TAG, "" + method.access_flag);
                Log.d(TAG, method.returnType);
                for(String type : method.paramTypes){
                    Log.d(TAG, type);
                }
                for(Annotation annotation : method.methodAnnotations){
                    Log.d(TAG, annotation.name);
                    for(AnnotationElement element : annotation.elements){
                        Log.d(TAG, element.name + ": " + element.value);
                    }
                }
            }
        });
    }

    public void dump(View view){
        dexer.dumpDex("storage/sdcard0/dump.txt");
    }
    
    private byte[] stream2byte(InputStream src) throws IOException {
        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = src.read(buffer))) {
            dest.write(buffer, 0, n);
        }
        return dest.toByteArray();
    }
}
