package com.ohelshem.api;

import com.github.kittinunf.result.Result;
import com.ohelshem.api.controller.declaration.ApiParser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public abstract class ApiCallback implements Function1<Result<ApiParser.ParsedData, Exception>, Unit> {
    @Override
    public Unit invoke(Result<ApiParser.ParsedData, Exception> result) {
        onResult(result);
        return null;
    }

    protected abstract void onResult(Result<ApiParser.ParsedData, Exception> result);
}
