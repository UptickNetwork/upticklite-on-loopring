// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.util;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.beetl.sql.core.engine.PageQuery;
import org.uptick.market.denum.Action;
import org.uptick.market.exception.ApiException;

import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
public class JsonResult<T> implements Serializable {
    private Integer code = 0;
    private Boolean success;
    private String msg;
    private T data;
    private JSONObject map = new JSONObject();

    public JsonResult() {

    }

    public JsonResult(boolean success, Integer code, String message) {
        this(success, code, message, null);
    }

    public JsonResult(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.msg = message;
        this.data = data;
    }


    public static <T> JsonResult<T> success() {
        return new JsonResult<>(true, Action.SUCCESS.getCode(), Action.SUCCESS.getMsg("en"));
    }

    public static <U> JsonResult<U> success(U data) {
        if (data instanceof PageQuery) {
            ((PageQuery) data).setParas(null);
        }
        return new JsonResult<>(true, Action.SUCCESS.getCode(), Action.SUCCESS.getMsg("en"), data);
    }

    public static <U> JsonResult<U> success(U data, Boolean paras) {
        if (paras == null || !paras) {
            if (data instanceof PageQuery) {

                ((PageQuery) data).setParas(null);
            }
        }
        return new JsonResult<>(true, Action.SUCCESS.getCode(), Action.SUCCESS.getMsg("en"), data);
    }


    public static <T> JsonResult<T> success(Action action, String lang) {
        return new JsonResult<>(true, action.getCode(), action.getMsg(lang));
    }


    public static <U> JsonResult<U> error(U data) {
        return new JsonResult<>(false, Action.ERROR.getCode(), Action.ERROR.getMsg("en"), data);

    }

    public static <U> JsonResult<U> error(ApiException exception) {
        return new JsonResult<>(false, exception.getCode(), exception.getMsg());

    }

    public static <T> JsonResult<T> error(Integer code, String message) {
        return new JsonResult<>(false, code, message);
    }

    public static <T> JsonResult<T> error(Action action, String lang) {
        return new JsonResult<>(false, action.getCode(), action.getMsg(lang));
    }

    public static <T> JsonResult<T> error(Action action) {
        return new JsonResult<>(false, action.getCode(), action.getMsg("en"));
    }


    public JsonResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }


    public JsonResult<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }


    public JsonResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }


    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public JsonResult<T> put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public JsonResult<T> putAll(Map<? extends String, ?> param) {
        this.map.putAll(param);
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
