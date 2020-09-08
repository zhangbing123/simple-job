package com.schedule.simplejob.model.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zhangbing
 * @create: 2020-09-07 10:44
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class QueryReq {

    private int page;//当前页

    private int limit;//每页大小

    public int getPage() {
        return page == 0 ? 1 : page;
    }

    public int getLimit() {
        return limit == 0 ? 10 : limit;
    }
}
