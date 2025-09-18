import request from '@/utils/request'
/**
 * 运营数据
 * @param pram
 */
 export function getOperationData(params) {
    return request({
      baseURL: "http://localhost:9002",
      url: '/system/dataPanel/getCarbonAssets',
      method: 'get',
      params
    })
  }
