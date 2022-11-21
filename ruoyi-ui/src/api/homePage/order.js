import request from '@/utils/request'

// ? 查询订单累计金额
export function queryOrderSum(query) {
    return request({
        url: '/home/orderMoney',
        method: 'get',
        params: query
    })
}

// ? 查询订单总量
export function queryOrderCount(query) {
    return request({
        url: '/home/orderCount',
        method: 'get',
        params: query
    })
}