package com.pokemoney.leaf;

import com.pokemoney.leaf.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
