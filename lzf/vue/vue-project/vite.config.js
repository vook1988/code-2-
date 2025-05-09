import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  server: {
    proxy: {
      // 代理所有以 /api 开头的请求
      '/api': {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,  // 解决本地开发证书问题
        rewrite: (path) => path.replace(/^\/api/, ''),
        // 添加详细日志输出
        configure: (proxy, options) => {
          proxy.on('error', (err, req, res) => {
            console.log('Proxy Error:', err)
          })
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('Proxy Request:', req.method, req.url)
          })
          proxy.on('proxyRes', (proxyRes, req, res) => {
            console.log('Proxy Response:', proxyRes.statusCode, req.url)
          })
        }
      }
    }
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})