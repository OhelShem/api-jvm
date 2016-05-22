package com.ohelshem.api;

import com.ohelshem.api.controller.declaration.ApiEngine;
import org.jetbrains.annotations.NotNull;

/**
 * Created by yoavst on 5/22/2016.
 */
public class Test {
    {
        ((ApiEngine) null).call("", "", 0, new Api.Callback() {
            @Override
            public void onSuccess(@NotNull Api.Response response) {

            }

            @Override
            public void onFailure(@NotNull Exception exception) {

            }
        });
    }
}
