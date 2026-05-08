<template>
	<el-container class="index-layout">
		<index-header></index-header>
		<el-container class="index-body">
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
	.index-layout {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
		width: 100%;
	}

	.index-body {
		display: flex;
		flex: 1;
		flex-direction: row;
		min-height: 0;
		width: 100%;
	}
</style>
