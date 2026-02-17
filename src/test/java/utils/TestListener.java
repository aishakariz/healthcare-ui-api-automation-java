package utils;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestListener implements AfterTestExecutionCallback {



    public void afterTestExecution(ExtensionContext context) throws Exception {


              if(context.getExecutionException().isPresent()){

                  BaseTest.attachScreenShot();
              }
        }


    }

