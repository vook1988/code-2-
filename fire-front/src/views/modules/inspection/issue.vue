<template>
  <div class="app-container">
    <div class="search-container">
      <el-form inline>
        <el-form-item label="名称">
          <el-input v-model="searchName" />
        </el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button type="primary" @click="openAddDialog">新增</el-button>
      </el-form>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column label="巡检名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="类目" align="center">
        <template slot-scope="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>
      <el-table-column label="地点" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.place }}
        </template>
      </el-table-column>
      <el-table-column label="巡检人" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡检时间" width="160" align="center">
        <template slot-scope="scope">
          {{ scope.row.displayTime }}
        </template>
      </el-table-column>
      <el-table-column label="问题描述" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.descLogging }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusFilterLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" :disabled="scope.row.status == '0'"
            @click="handle(scope.row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="800px">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="任务名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类目">
          <el-select v-model="form.type" placeholder="请选择类目">
            <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检人">
          <el-select v-model="form.author" placeholder="请选择巡检人">
            <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="form.outcome" type="textarea" />
        </el-form-item>
        <el-form-item label="处理照片">
          <el-upload
            action="https://jsonplaceholder.typicode.com/posts/"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-success="handleUploadSuccess"
            ref="upload">
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>
        <el-dialog :visible.sync="dialogVisibleImg">
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
        <el-form-item label="处理人">
          <el-input v-model="form.user" />
        </el-form-item>
        <el-form-item label="巡检时间">
          <el-date-picker
            v-model="form.displayTime"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="{
              shortcuts: false,
              format: 'yyyy-MM-dd HH:mm:ss',
              valueFormat: 'yyyy-MM-dd HH:mm:ss'
            }"
          />
        </el-form-item>
        <el-form-item label="处理时间">
          <el-date-picker
            v-model="form.dealTime"
            type="datetime"
            placeholder="选择日期时间"
            :picker-options="{
              shortcuts: false,
              format: 'yyyy-MM-dd HH:mm:ss',
              valueFormat: 'yyyy-MM-dd HH:mm:ss'
            }"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="已处理" value="0" />
            <el-option label="未处理" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="form.place" />
        </el-form-item>
        <el-form-item label="问题描述">
          <el-input v-model="form.descLogging" type="textarea" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button type="primary" plain @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'success',
        1: 'danger'
      }
      return statusMap[status]
    },
    statusFilterLabel(status) {
      const statusMap = {
        0: '已处理',
        1: '未处理'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      data: [],
      dialogVisible: false,
      dialogTitle: '',
      personList: [{ label: '张三', value: '0' }],
      numList: [{ label: '每年', value: '0' }, { label: '每月', value: '1' }, { label: '每日', value: '2' }],
      typeList: [{ label: '消防安全', value: '0' }, { label: '食堂安全', value: '1' }, { label: '护工查房', value: '2' }],
      form: {},
      processVoList: [
        { status: 0, processName: '东门', percent: '漏检' },
        { status: 1, processName: '西门', percent: '已检', time: '2023-12-24 16:10:10' },
        { status: 2, processName: '北门', percent: '等待' }
      ],
      dialogImageUrl: '',
      dialogVisibleImg: false,
      // 分页相关数据
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchName: '',
      uploadImageUrl: '' // 存储上传图片的 URL
    }
  },
  created() {
    this.getPageList()
  },
  methods: {
    handleSizeChange(newSize) {
      this.pageSize = newSize
      this.getPageList()
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage
      this.getPageList()
    },
    getPageList() {
      // 定义查询参数
      const params = {
        pageNo: this.currentPage,
        pageSize: this.pageSize,
        name: this.searchName
      };
      this.listLoading = true;
      this.$http({
        url: this.$http.adornUrl('/inspection/problemLogging/list'),
        method: 'get',
        // 传递查询参数
        params: this.$http.adornParams(params)
      }).then(({ data }) => {
        // 处理返回的数据
        if (data.code === 0) {
          this.list = data.page.records
          this.total = data.page.total
        }
        this.listLoading = false
      }).catch((error) => {
        // 处理请求错误
        console.error('请求失败:', error);
        this.listLoading = false;
      });
    },
    openAddDialog() {
      this.dialogVisible = true
      this.dialogTitle = '新增问题记录'
      this.form = {
        name: '',
        type: '',
        author: '',
        outcome: '',
        image: '',
        user: '',
        displayTime: '',
        dealTime: '',
        status: '',
        place: '',
        descLogging: '',
        createBy: '',
        updateBy: ''
      }
      this.uploadImageUrl = '' // 清空上传图片的 URL
      this.$refs.upload.clearFiles() // 清空上传文件列表
    },
    handle(row) {
      console.log(row)
      this.dialogVisible = true
      this.dialogTitle = '巡检问题处理'
      this.form = JSON.parse(JSON.stringify(row))
    },
    handleUploadSuccess(response, file, fileList) {
      // 假设返回的 response 中有图片的 URL
      this.uploadImageUrl = response.url || ''
    },
    submitForm() {
      // 格式化日期
      if (this.form.displayTime) {
        const displayDate = new Date(this.form.displayTime);
        this.form.displayTime = `${displayDate.getFullYear()}-${String(displayDate.getMonth() + 1).padStart(2, '0')}-${String(displayDate.getDate()).padStart(2, '0')} ${String(displayDate.getHours()).padStart(2, '0')}:${String(displayDate.getMinutes()).padStart(2, '0')}:${String(displayDate.getSeconds()).padStart(2, '0')}`;
      }
      if (this.form.dealTime) {
        const dealDate = new Date(this.form.dealTime);
        this.form.dealTime = `${dealDate.getFullYear()}-${String(dealDate.getMonth() + 1).padStart(2, '0')}-${String(dealDate.getDate()).padStart(2, '0')} ${String(dealDate.getHours()).padStart(2, '0')}:${String(dealDate.getMinutes()).padStart(2, '0')}:${String(dealDate.getSeconds()).padStart(2, '0')}`;
      }

      // 设置图片 URL
      this.form.image = this.uploadImageUrl;

      this.$http({
        url: this.$http.adornUrl('/inspection/problemLogging/add'),
        method: 'post',
        data: this.$http.adornData(this.form, false)
      }).then(({ data }) => {
        if (data.code === 0) {
          this.$message.success('添加成功')
          this.dialogVisible = false
          this.getPageList()
        } else {
          this.$message.error('添加失败')
        }
      }).catch((error) => {
        console.error('请求失败:', error)
        this.$message.error('添加失败')
      })
    },
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.dialogVisible = false
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
      this.uploadImageUrl = '' // 清空上传图片的 URL
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisibleImg = true
    },
    search() {
      this.currentPage = 1
      this.getPageList()
    }
  }
}
</script>

<style scoped lang="scss">
.line {
  text-align: center;
}

.el-step {

  ::v-deep .el-step__line {
    top: 18px;
  }

  ::v-deep .el-step__head.is-success .el-step__icon.is-text {
    color: #fff !important;
    background: #67c23a !important;
  }
}
</style>