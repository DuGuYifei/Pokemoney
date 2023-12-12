package com.pokemoney.leaf.common;

import com.pokemoney.leaf.IDGen;

public class ZeroIDGen implements IDGen {
    @Override
    public Result get(String key) {
        return new Result(0, Status.SUCCESS);
    }

    @Override
    public boolean init() {
        return true;
    }
}
