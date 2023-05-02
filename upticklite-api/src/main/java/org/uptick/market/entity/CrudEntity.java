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
package org.uptick.market.entity;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel
public abstract class CrudEntity implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Boolean deleted;

    @ApiModelProperty(hidden = true)
    private Integer weight;

    @ApiModelProperty(hidden = true)
    private Integer version;

    @ApiModelProperty(hidden = true)
    private String json;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public void addJsonProperty(String k, String v) {
        if (StringUtils.isEmpty(json)) {
            this.setJson("{}");
        }
        JSONObject jsonObject = JSONObject.parseObject(this.getJson());
        jsonObject.put(k, v);
        this.setJson(jsonObject.toJSONString());
    }

    public Object getJsonProperty(String key) {
        String json = getJson();
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            return JSONObject.parseObject(json).get(key);
        }
    }
}
