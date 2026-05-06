<template>
	<el-container>
		<index-header></index-header>
		<el-container>
			<index-aside></index-aside>
			<index-main></index-main>
		</el-container>
	</el-container>
</template>
<script>
	import IndexHeader from '@/components/index/IndexHeader'
	import IndexAside from '@/components/index/IndexAsideStatic'
	import IndexMain from '@/components/index/IndexMain'
	export default {
		components: {
			IndexHeader,
			IndexAside,
			IndexMain
		}
	}
</script>

<style lang="scss" scoped>
	.el-container {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
		width: 100%;
	}
</style>
