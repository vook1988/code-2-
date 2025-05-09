<template>
  <div class="app-container">
      <div class="search-container">
          <el-form inline>
              <el-form-item label="名称">
                  <el-input />
              </el-form-item>
              <el-button type="primary">搜索</el-button>
              <el-button type="primary" @click="operate('add')">新增</el-button>
          </el-form>
      </div>
      <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
          <el-table-column align="center" label="ID" width="95">
              <template slot-scope="scope">
                  {{ scope.$index }}
              </template>
          </el-table-column>
          <el-table-column label="名称">
              <template slot-scope="scope">
                  {{ scope.row.name }}
              </template>
          </el-table-column>
          <el-table-column label="点位">
              <template slot-scope="scope">
                  {{ scope.row.place }}
              </template>
          </el-table-column>
          <el-table-column label="操作" width="220" align="center" fixed="right">
              <template slot-scope="scope">
                  <el-button type="success" size="mini" @click="operate('edit', scope.row)">修改</el-button>
                  <el-button type="danger" size="mini">删除</el-button>
              </template>
          </el-table-column>
      </el-table>
      <!-- 添加分页组件 -->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
      <el-dialog :visible.sync="dialogVisible" :title="title" width="500px">
          <el-form ref="form" :model="form" label-width="120px">
              <el-form-item label="名称">
                  <el-input v-model="form.name" />
              </el-form-item>
              <el-form-item v-for="(item, index) in form.itemList" :key="index" :label="'点位' + index"
                  :prop="'itemList.' + index + '.value'">
                  <el-input v-model="item.value" style="width: 200px;margin-right: 20px;" />
                  <el-button v-if="index != 0" type="danger" @click.prevent="removeplace(item)">删除</el-button>
              </el-form-item>
              <el-form-item>
                  <el-button type="primary" @click="addplace">新增点位</el-button>
                  <el-button type="primary" @click="onSubmit">提交</el-button>
                  <el-button @click="onCancel">取消</el-button>
              </el-form-item>
          </el-form>
      </el-dialog>
  </div>
</template>

<script>
// import { getList } from '@/api/table'

export default {
  filters: {
      statusFilter(status) {
          const statusMap = {
              0: 'success',
              1: 'info'
          }
          return statusMap[status]
      },
      statusFilterLabel(status) {
          const statusMap = {
              0: '在职',
              1: '离职'
          }
          return statusMap[status]
      }
  },
  data() {
      return {
          list: null,
          listLoading: true,
          data: [{
              name: '食堂巡查',
              place: '食堂东门'
          }, {
              name: '食堂巡查',
              place: '食堂西门'
          }, {
              name: '食堂巡查',
              place: '食堂南门'
          }],
          dialogVisible: false,
          title: '新增线路',
          personList: [{ label: '张三', value: '0' }],
          numList: [{ label: '食堂路线', value: '0' }, { label: '消防路线', value: '1' }, { label: '查房路线', value: '2' }],
          typeList: [{ label: '消防安全', value: '0' }, { label: '食堂安全', value: '1' }, { label: '护工查房', value: '2' }],
          form: {
              itemList: [
                  { value: '' }
              ]
          },
          // 分页相关数据
          currentPage: 1,
          pageSize: 10,
          total: 0
      }
  },
  created() {
      this.fetchData()
  },
  methods: {
      fetchData() {
          this.getPageList()
      },
      operate(type, row) {
          if (type === 'add') {
              this.title = '新增线路'
              this.dialogVisible = true
              this.form = {
                  itemList: [
                      { value: '' }
                  ]
              }
          } else if (type === 'edit') {
              this.dialogVisible = true
              this.title = '修改线路'
              this.form = JSON.parse(JSON.stringify(row))
          }
      },
      onSubmit() {
          this.$message('submit!')
      },
      onCancel() {
          this.dialogVisible = false
      },
      addplace() {
          this.form.itemList.push({
              value: '',
              key: Date.now()
          })
      },
      removeplace(item) {
          var index = this.form.itemList.indexOf(item)
          if (index !== -1) {
              this.form.itemList.splice(index, 1)
          }
      },
      getPageList() {
          // 定义查询参数
          const params = {
              pageNo: this.currentPage,
              pageSize: this.pageSize
          };
          this.listLoading = true;
          this.$http({
              url: this.$http.adornUrl('/inspection/route/list'),
              method: 'get',
              // 传递查询参数
              params: this.$http.adornParams(params)
          }).then(({ data }) => {
              // 处理返回的数据
              this.list = data.page.records
              this.total = data.page.total
              this.listLoading = false;
              console.log("dxm", data);
          }).catch((error) => {
              // 处理请求错误
              console.error('请求失败:', error);
              this.listLoading = false;
          });
      },
      // 处理每页数量变化
      handleSizeChange(newSize) {
          this.pageSize = newSize
          this.getPageList()
      },
      // 处理当前页码变化
      handleCurrentChange(newPage) {
          this.currentPage = newPage
          this.getPageList()
      }
  }
}
</script>
<style scoped>
.line {
  text-align: center;
}
</style>