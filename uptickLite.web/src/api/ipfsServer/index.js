import Vue from "vue";
// import { Subject } from "rxjs";
import { create, IPFSHTTPClient } from "ipfs-http-client";

const IPFS_LOOPRING_URL = "https://d1vjs0p75nt8te.cloudfront.net";

let _ipfs;

// (function () {
//     try {
//         _ipfs = await create({
//             url: `${IPFS_LOOPRING_URL}`,
//         });
//     } catch (error) {
//         //   console.error("IPFSHTTPClient ERROR ON INIT:", error);
//         //   ipfsService.sendError(new CustomError(ErrorMap.CREATE_IPFS_ERROR));
//     }
// })();

export async function addFile(file) {
    if (_ipfs && file) {
        let res = await _ipfs
            .add({ content: file.stream() })
            .catch((e) => {
                throw e;
            });
        return res;
    }
    return null;
}

export async function addJSON(json) {
    if (_ipfs ) {
        let res = await _ipfs
            .add(json)
            .catch((e) => {
                throw e;
            });
        return res;
    }
    return null;
}



export default async function init() {
    try {
        _ipfs = await create({
            url: `${IPFS_LOOPRING_URL}`,
        });
    } catch (error) {
        //   console.error("IPFSHTTPClient ERROR ON INIT:", error);
        //   ipfsService.sendError(new CustomError(ErrorMap.CREATE_IPFS_ERROR));
    }
    
    Vue.prototype.ipfsAddFile = addFile;
    Vue.prototype.ipfsAddJSON = addJSON;
}