const base = {
    get() {
        return {
            url : "http://localhost:8080/springbootk73q9/",
            name: "springbootk73q9",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/springbootk73q9/front/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "校友社交系统"
        } 
    }
}
export default base
