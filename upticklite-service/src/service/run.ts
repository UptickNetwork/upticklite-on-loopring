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
/* Imports: External */
import * as dotenv from 'dotenv'
import Config from 'bcfg'

/* Imports: Internal */
import { DataTransportService } from './main/service'
import browserEnv from 'browser-env';

interface Bcfg {
    load: (options: { env?: boolean; argv?: boolean }) => void
    str: (name: string, defaultValue?: string) => string
    uint: (name: string, defaultValue?: number) => number
    bool: (name: string, defaultValue?: boolean) => boolean
}


;(async () => {
    
    browserEnv(['navigator']);
    
    dotenv.config()
    const config: Bcfg = new Config('uptick-lrc')
    config.load({
        env: true,
        argv: true,
    })

    const service = new DataTransportService({


        serverHost :  config.str('serverHost', ""),
        serverPort :  config.uint('serverPort', 8901),

        lrcConf:{
            adminAddress:   config.str('adminAddress', ''),
            adminPRIV:      config.str('adminPRIV', ''),
            chainId:        config.uint('chainId',1),
            infroUrl:       config.str('infroUrl', '')
        },

    })

    await service.start()
    


})()
