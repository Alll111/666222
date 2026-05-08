<template>
  <div id="app" class="">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: "app",
};
</script>

<style lang="scss">
*{
  padding: 0;
  margin:0;
}
html,body{
  width: 100%;
  height: 100%;
}
#app{
  height:100%;
}
body {
  padding: 0;
  margin: 0;
  background: linear-gradient(180deg, #edf4ff 0%, #f7fbff 40%, #f3f7fd 100%);
  color: #1f2d3d;
}

html {
  scroll-behavior: smooth;
}

body,
button,
input,
textarea,
select {
  font-family: "Microsoft YaHei", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
}

#app,
.el-container {
  min-height: 100%;
}
</style>
