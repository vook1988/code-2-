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
      <el-table-column label="巡检名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="类目">
        <template slot-scope="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>
      <el-table-column label="地点">
        <template slot-scope="scope">
          {{ scope.row.place }}
        </template>
      </el-table-column>
      <el-table-column label="巡检人" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡检时间" width="200" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" /> <span>{{ scope.row.displayTime }}</span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusFilterLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="detail(scope.row)">详情</el-button>
          <el-button type="danger" size="mini" @click="deleteItem(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
      <el-form ref="formRef" :model="form" label-width="120px">
        <el-form-item label="巡检任务名称" :rules="{ required: true, message: '请输入巡检任务名称', trigger: 'blur' }">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类目" :rules="{ required: true, message: '请选择类目', trigger: 'change' }">
          <el-select v-model="form.type" placeholder="请选择类目">
            <el-option 
              v-for="item in typeList" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点" :rules="{ required: true, message: '请输入地点', trigger: 'blur' }">
          <el-input v-model="form.place" />
        </el-form-item>
        <el-form-item label="巡检人" :rules="{ required: true, message: '请选择巡检人', trigger: 'change' }">
          <el-select v-model="form.author" placeholder="请选择巡检人">
            <el-option 
              v-for="item in personList" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡检时间" :rules="{ required: true, message: '请选择巡检时间', trigger: 'change' }">
          <el-date-picker 
            v-model="form.displayTime" 
            type="datetime" 
            placeholder="选择日期时间" 
            :picker-options="{
              shortcuts: false,
              format: 'yyyy-MM-dd HH:mm:ss',
              valueFormat: 'yyyy-MM-dd HH:mm:ss'
            }" />
        </el-form-item>
        <el-form-item label="状态" :rules="{ required: true, message: '请选择状态', trigger: 'change' }">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option 
              v-for="item in statusOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="电话" :rules="{ required: true, message: '请输入电话', trigger: 'blur' }">
          <el-input v-model="form.phone" />
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="onCancel">取消</el-button>
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
        1: 'danger',
        2: 'primary',
        3: 'warning'
      };
      return statusMap[status];
    },
    statusFilterLabel(status) {
      const statusMap = {
        0: '漏检',
        1: '待检',
        2: '已检',
        3: '暂停中'
      };
      return statusMap[status];
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      dialogVisible: false,
      dialogTitle: '',
      personList: [], // 初始化为空数组，将从机器人接口获取
      numList: [
        { label: '每年', value: '0' }, 
        { label: '每月', value: '1' }, 
        { label: '每日', value: '2' }
      ],
      typeList: [
        { label: '消防安全', value: '0' }, 
        { label: '食堂安全', value: '1' }, 
        { label: '护工查房', value: '2' }
      ],
      form: {},
      processVoList: [
        { status: 0, processName: '漏检', percent: '漏检' },
        { status: 1, processName: '待检', percent: '待检' },
        { status: 2, processName: '已检', percent: '已检' },
        { status: 3, processName: '暂停中', percent: '暂停中' }
      ],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchName: '',
      statusOptions: [
        { label: '漏检', value: 0 },
        { label: '待检', value: 1 },
        { label: '已检', value: 2 },
        { label: '暂停中', value: 3 }
      ]
    };
  },
  created() {
    this.fetchData();
    this.fetchRobotList(); // 组件创建时获取机器人列表
  },
  methods: {
    // 获取机器人列表
    fetchRobotList() {
      this.$http({
        url: this.$http.adornUrl('/robot/list'),
        method: 'get',
        params: this.$http.adornParams({
          pageNum: 1,
          pageSize: 1000 // 获取足够多的数据
        })
      }).then(({ data }) => {
        console.log('机器人列表数据:', data);
        if (data.success) {
          this.personList = data.data.map(robot => ({
            label: robot.robotCode,
            value: robot.robotCode  
          }));
        }
      }).catch(error => {
        console.error('获取机器人列表失败:', error);
        this.$message.error('获取巡检人列表失败');
      });
    },
    
    fetchData() {
      this.getPageList();
    },
    
    detail(row) {
      this.dialogVisible = true;
      this.dialogTitle = '巡检任务详情';
      this.form = JSON.parse(JSON.stringify(row));
      this.updateProcessVoList(row.status, row.displayTime);
    },
    
    openAddDialog() {
      this.dialogVisible = true;
      this.dialogTitle = '新增巡检任务';
      this.form = {
        name: '',
        type: '',
        place: '',
        author: '',
        displayTime: '',
        status: '',
        phone: ''
      };
    },
    
    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 格式化日期时间
          if (this.form.displayTime) {
            const date = new Date(this.form.displayTime);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            this.form.displayTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
          }

          this.$http({
            url: this.$http.adornUrl('/inspection/inspectionTask/add'),
            method: 'post',
            data: this.form
          }).then(({ data }) => {
            if (data.code === 0) {
              this.$message({
                message: '新增巡检任务成功',
                type: 'success',
                duration: 2000,
                onClose: () => {
                  this.dialogVisible = false;
                  this.getPageList();
                }
              });
            } else {
              this.$message.error(data.msg);
            }
          }).catch((error) => {
            console.error('请求失败:', error);
            this.$message.error('添加失败，请稍后重试');
          });
        } else {
          this.$message.error('请完善表单信息');
          return false;
        }
      });
    },
    
    onCancel() {
      this.dialogVisible = false;
    },
    
    getPageList() {
      const params = {
        pageNo: this.currentPage,
        pageSize: this.pageSize,
        name: this.searchName
      };

      this.listLoading = true;
      this.$http({
        url: this.$http.adornUrl('/inspection/inspectionTask/list'),
        method: 'get',
        params: this.$http.adornParams(params)
      }).then(({ data }) => {
        this.list = data.page.records;
        this.total = data.page.total;
        this.listLoading = false;
      }).catch((error) => {
        console.error('请求失败:', error);
        this.listLoading = false;
      });
    },
    
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.getPageList();
    },
    
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.getPageList();
    },
    
    updateProcessVoList(status, displayTime) {
      this.processVoList.forEach((item) => {
        if (item.status === status) {
          item.status = status;
          item.time = displayTime;
        }
      });
    },
    
    search() {
      this.currentPage = 1;
      this.getPageList();
    },
    
    deleteItem(id) {
      if (confirm('确定要删除该记录吗？')) {
        const longId = Number(id);
        this.$http({
          url: this.$http.adornUrl('/inspection/inspectionTask/delete'),
          method: 'delete',
          params: { id: longId }
        }).then(({ data }) => {
          if (data.code === 0) {
            this.$message.success('删除成功');
            this.getPageList();
          } else {
            this.$message.error('删除失败');
          }
        }).catch((error) => {
          console.error('删除失败:', error);
          this.$message.error('删除失败');
        });
      }
    }
  }
};
</script>

<style scoped lang="scss">
.line {
  text-align: center;
}

.el-step {
  .el-step__head {
    :deep(.is-success) {}
  }

  ::v-deep .el-step__line {
    top: 18px;
  }

  ::v-deep .el-step__head.is-success .el-step__icon.is-text {
    color: #fff !important;
    background: #67c23a !important;
  }
}
</style>