const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

function publicPath() {
  return process.env.NODE_ENV === 'production' ? '././' : '/'
}

module.exports = {
  publicPath: publicPath(),
  configureWebpack: {
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  },
  lintOnSave: false,
  devServer: {
    host: '0.0.0.0',
    port: 8081,
    open: true,
    hot: true,
    https: false,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  },
  chainWebpack(config) {
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()

    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
  }
}
