import axios from "axios";
import { ElMessage } from 'element-plus';

axios.defaults.baseURL = "http://127.0.0.1:8081";

export function message(msg, type) {
    ElMessage({
        message: msg,
        type: type,
        center: true,
        showClose: true,
    })
}

export const getRequest = (url, data) => {
    return axios({
        method: 'get',
        url: url,
        data: data,
    })
}

export const postRequest = (url, data) => {
    return axios({
        method: 'post',
        url: url,
        data: data,
    })
}
