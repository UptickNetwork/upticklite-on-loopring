
import Encrypt from 'encryptlong'
const Base64 = require('js-base64').Base64

//公钥
var signPrivateKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgrrvnRh8yqNEFyc3F3oen2P4R2hcEl2C/QAbd8XqIqoGjo7DZfI1vSpu44NeD6zEeukg1vmMUbB+WfwVAMSm6G0MFbVvtu2Aapbhu/mQ9ub1175ej0wVl/L1ZS1qMPTYCvlBmCtegWXyoq9igDtp6DB2BZTMJgH4fSg3MdUD85QIDAQAB';


export default {
    encrypt: function (data) {
        let encrypt = new Encrypt();
        encrypt.setPublicKey(signPrivateKey);
        let info = encrypt.encryptLong(JSON.stringify(data))
        const exStr = Base64.encode(info)//加密
        return exStr
    },
    
}