package com.ohelshem.api;

import com.github.kittinunf.result.Result;
import com.ohelshem.api.controller.declaration.ApiParser;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public abstract class ApiCallback implements Function1<Result<ApiParser.ParsedData, ? extends Exception>, Unit> {
    @Override
    public final Unit invoke(Result<ApiParser.ParsedData, ? extends Exception> result) {
        onResult(result);
        return Unit.INSTANCE;
    }

    protected abstract void onResult(Result<ApiParser.ParsedData, ? extends Exception> result);
}
