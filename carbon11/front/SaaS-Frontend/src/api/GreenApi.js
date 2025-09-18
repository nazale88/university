import request from '@/utils/request'

export function loadGreenIndex(){
    return request({
            baseURL: "http://localhost:9002",
            url: '/system/dataPanel/getGreenness',
            method: 'get'
    })
}
