<template>
<div class="content">
			<div class="text main-text" :style='{"padding":"0","boxShadow":"0 0 0px rgba(0,0,0,.1)","margin":"0 auto","borderColor":"rgba(0,0,0,.3)","backgroundColor":"rgba(247, 247, 247, 0)","color":"rgba(0, 0, 0, 1)","borderRadius":"0","borderWidth":"0","width":"650px","lineHeight":"50px","fontSize":"26px","borderStyle":"solid"}'>欢迎使用 {{this.$project.projectName}}</div>
	</div>
</template>
<script>
export default {
	data() {
		return {
			
		};
	},
  mounted(){
    this.init();
  },
  methods:{
    init(){
        const gotoLogin = () => {
          if (this.$router.currentRoute.value.name !== 'login') {
            this.$router.push({ name: 'login' }).catch(() => {})
          }
        }
        if(this.$storage.get('Token')){
        const sessionTable = this.$storage.get('sessionTable') || 'users'
        this.$http({
            url: `${sessionTable}/session`,
            method: "get"
        }).then(({ data }) => {
            if (data && data.code != 0) {
            gotoLogin()
            }
        }).catch(() => {
            gotoLogin()
        });
        }else{
            gotoLogin()
        }
    }
  }
};
</script>

<style lang="scss" scoped>
.content {
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  min-height: 500px;
  text-align: center;
  background: transparent;
  .main-text{
    font-size: 38px;
    font-weight: bold;
    margin-top: 12%;
  }
  .text {
    font-size: 24px;
    font-weight: bold;
    color: #333;
  }
}
</style>
