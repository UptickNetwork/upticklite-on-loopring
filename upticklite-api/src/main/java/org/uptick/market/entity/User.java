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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;

/**
 * user info
 */

@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class User extends CrudEntity {

    private static final long serialVersionUID = 123456789L;

    @ApiModelProperty(required = true, value = "Loopring account status(1:unregistered；2：Registered, not activated；3：activated)", example = "1")
    private Integer accountStatus;


    @ApiModelProperty(required = false, value = "Loopring account status", example = "12542")
    private Long accountId;

    @ApiModelProperty(required = true, value = "Loopring wallet address", example = "0xb731a264d86714adcfc5841ba58db518b8bc142a")
    private String address;

    @ApiModelProperty(required = false, value = "User description", example = "")
    private String description;

    @ApiModelProperty(required = false, value = "User e-mail", example = "")
    private String email;


    @ApiModelProperty(required = false, value = "User name", example = "")
    private String name;

    @ApiModelProperty(required = false, value = "User profile photo", example = "")
    private String profilePhoto;

    @ApiModelProperty(required = false, value = "User background image", example = "")
    private String background;


}
