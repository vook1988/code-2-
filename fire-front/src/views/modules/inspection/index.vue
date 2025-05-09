<template>
  <div class="app-container">
    <div class="search-container">
      <el-form inline>
        <el-form-item label="名称">
          <el-input v-model="searchName" />
        </el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button type="primary" @click="operate('add')">新增</el-button>
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
          {{ scope.row.place || '暂无地点信息' }}
        </template>
      </el-table-column>
      <el-table-column label="巡检人" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column label="巡检时间" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.displayTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusFilterLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="310" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" :disabled="scope.row.status == 0">发布</el-button>
          <el-button type="warning" size="mini" :disabled="scope.row.status == 1">停用</el-button>
          <el-button type="success" size="mini" @click="operate('edit', scope.row)">修改</el-button>
          <el-button type="danger" size="mini" @click="deleteItem(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
    <el-dialog :visible.sync="dialogVisible" :title="title" width="800px">
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="巡检计划名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="巡查人">
          <el-select v-model="form.person" placeholder="请选择巡查人">
            <el-option v-for="(item, index) in personList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="类目">
          <el-select v-model="form.region" placeholder="请选择类目">
            <el-option v-for="(item, index) in typeList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="form.place" />
        </el-form-item>
        <el-form-item label="巡查频次">
          <el-select v-model="form.num" placeholder="请选择巡查频次">
            <el-option v-for="(item, index) in numList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡查时间">
          <el-col :span="11">
            <el-date-picker v-model="form.date1" type="date" placeholder="选择日期" style="width: 100%;" />
          </el-col>
          <el-col :span="2" class="line">-</el-col>
          <el-col :span="11">
            <el-time-picker v-model="form.date2" type="fixed-time" placeholder="选择时间" style="width: 100%;" />
          </el-col>
        </el-form-item>
        <el-form-item label="是否发布">
          <el-switch v-model="form.delivery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
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
        1: 'gray',
        2: 'danger'
      };
      return statusMap[status];
    },
    statusFilterLabel(status) {
      const statusMap = {
        0: '已发布',
        1: '未发布',
        2: '暂停中'
      };
      return statusMap[status];
    }
  },
  data() {
    return {
      list: [],
      listLoading: true,
      dialogVisible: false,
      title: '新增巡检计划',
      personList: [{ label: '张三', value: '0' }],
      numList: [{ label: '每年', value: '0' }, { label: '每月', value: '1' }, { label: '每日', value: '2' }],
      typeList: [{ label: '消防安全', value: '0' }, { label: '食堂安全', value: '1' }, { label: '护工查房', value: '2' }],
      form: {
        id: null, // 新增 id 字段，用于修改时传递
        name: '',
        person: '',
        region: '',
        place: '',
        num: '',
        date1: null,
        date2: null,
        delivery: false
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchName: '',
      operateType: 'add' // 新增操作类型字段，用于区分新增和修改
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      // 定义查询参数
      const params = {
        pageNo: this.currentPage,
        pageSize: this.pageSize,
        name: this.searchName
      };

      this.$http({
        url: this.$http.adornUrl('/inspection/patrolPlan/list'),
        method: 'get',
        // 传递查询参数
        params: this.$http.adornParams(params)
      }).then(({ data }) => {
        if (data.code === 0) {
          this.list = data.page.records;
          this.total = data.page.total;
        }
        this.listLoading = false;
      }).catch((error) => {
        // 处理请求错误
        console.error('请求失败:', error);
        this.listLoading = false;
      });
    },
    operate(type, row) {
      this.operateType = type;
      if (type === 'add') {
        this.title = '新增巡检计划';
        this.form = {
          id: null,
          name: '',
          person: '',
          region: '',
          place: '',
          num: '',
          date1: null,
          date2: null,
          delivery: false
        };
      } else if (type === 'edit') {
        this.title = '修改巡检计划';
        this.form = {
          id: row.id,
          name: row.name,
          person: this.getPersonValue(row.author),
          region: this.getTypeValue(row.type),
          place: row.place,
          num: row.num,
          date1: this.getDatePart(row.displayTime),
          date2: this.getTimePart(row.displayTime),
          delivery: row.status === 0
        };
      }
      this.dialogVisible = true;
    },
    onSubmit() {
      // 检查日期和时间是否都已选择
      if (!this.form.date1 || !this.form.date2) {
        this.$message.error('请选择巡查日期和时间');
        return;
      }
      // 构建请求数据
      const requestData = {
        id: this.form.id,
        name: this.form.name,
        author: this.getPersonLabel(this.form.person),
        type: this.getTypeLabel(this.form.region),
        place: this.form.place,
        // 这里需要根据实际情况处理巡查时间，假设后端需要字符串格式
        displayTime: `${this.formatDate(this.form.date1)} ${this.formatTime(this.form.date2)}`,
        status: this.form.delivery ? 0 : 1
      };

      let url = '';
      if (this.operateType === 'add') {
        url = this.$http.adornUrl('/inspection/patrolPlan/add');
      } else if (this.operateType === 'edit') {
        url = this.$http.adornUrl('/inspection/patrolPlan/edit');
      }

      this.$http({
        url: url,
        method: this.operateType === 'add' ? 'post' : 'put',
        data: requestData
      }).then(({ data }) => {
        if (data.code === 0) {
          this.$message.success(data.msg);
          this.dialogVisible = false;
          // 重新获取列表数据
          this.fetchData();
        } else {
          this.$message.error(data.msg);
        }
      }).catch((error) => {
        console.error(`${this.operateType === 'add' ? '新增' : '修改'}失败:`, error);
        this.$message.error(`${this.operateType === 'add' ? '新增' : '修改'}失败，请稍后重试`);
      });
    },
    onCancel() {
      this.dialogVisible = false;
    },
    // 处理每页显示数量变化事件
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.fetchData();
    },
    // 处理当前页码变化事件
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.fetchData();
    },
    // 根据值获取人员标签
    getPersonLabel(value) {
      const person = this.personList.find(item => item.value === value);
      return person ? person.label : '';
    },
    // 根据值获取类目标签
    getTypeLabel(value) {
      const type = this.typeList.find(item => item.value === value);
      return type ? type.label : '';
    },
    // 根据标签获取人员值
    getPersonValue(label) {
      const person = this.personList.find(item => item.label === label);
      return person ? person.value : '';
    },
    // 根据标签获取类目值
    getTypeValue(label) {
      const type = this.typeList.find(item => item.label === label);
      return type ? type.value : '';
    },
    // 格式化日期
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    // 格式化时间
    formatTime(time) {
      if (!time) return '';
      let timeStr;
      if (typeof time === 'string') {
        timeStr = time;
      } else if (time instanceof Date) {
        const hours = String(time.getHours()).padStart(2, '0');
        const minutes = String(time.getMinutes()).padStart(2, '0');
        const seconds = String(time.getSeconds()).padStart(2, '0');
        timeStr = `${hours}:${minutes}:${seconds}`;
      } else {
        timeStr = String(time);
      }
      const parts = timeStr.split(':');
      const hours = parts[0].padStart(2, '0');
      const minutes = parts[1].padStart(2, '0');
      const seconds = parts.length > 2 ? parts[2].padStart(2, '0') : '00';
      return `${hours}:${minutes}:${seconds}`;
    },
    // 获取日期部分
    getDatePart(dateTime) {
      if (!dateTime) return null;
      return dateTime.split(' ')[0];
    },
    // 获取时间部分
    getTimePart(dateTime) {
      if (!dateTime) return null;
      return dateTime.split(' ')[1];
    },
    // 删除方法
    deleteItem(id) {
      if (confirm('确定要删除该记录吗？')) {
        this.$http({
          url: this.$http.adornUrl('/inspection/patrolPlan/delete'),
          method: 'delete',
          params: { id }
        }).then(({ data }) => {
          if (data.code === 0) {
            this.$message.success('删除成功');
            this.fetchData();
          } else {
            this.$message.error('删除失败');
          }
        }).catch((error) => {
          console.error('删除失败:', error);
          this.$message.error('删除失败');
        });
      }
    },
    // 搜索方法
    search() {
      this.currentPage = 1;
      this.fetchData();
    }
  }
};
</script>

<style scoped>
.line {
  text-align: center;
}
</style>