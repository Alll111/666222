const base = {
    get() {
        return {
            url : "/api/",
            name: "api",
            // 退出到首页链接
            indexUrl: '/'
        };
    },
    getProjectName(){
        return {
            projectName: "校友社交系统"
        } 
    }
}
export default base
