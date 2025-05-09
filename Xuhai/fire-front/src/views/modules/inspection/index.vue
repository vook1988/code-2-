<template>
  <div class="app-container">
    <div class="search-container">
      <el-form inline>
        <el-form-item label="名称">
          <el-input v-model="searchName" placeholder="请输入巡检计划名称" clearable />
        </el-form-item>
        <el-button type="primary" @click="search">搜索</el-button>
        <el-button type="primary" @click="operate('add')">新增</el-button>
      </el-form>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
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
          <!-- <el-button type="warning" size="mini" :disabled="scope.row.status == 1" @click="pauseItem(scope.row.id)">停用</el-button> -->
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
    <el-dialog :visible.sync="dialogVisible" :title="title" width="800px" @close="onCancel">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="巡检计划名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入巡检计划名称" />
        </el-form-item>
        <el-form-item label="巡查人" prop="person">
          <el-select v-model="form.person" placeholder="请选择巡查人" style="width: 100%">
            <el-option v-for="(item, index) in personList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="类目" prop="region">
          <el-select v-model="form.region" placeholder="请选择类目" style="width: 100%">
            <el-option v-for="(item, index) in typeList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点" prop="place">
          <el-input v-model="form.place" placeholder="请输入巡查地点" />
        </el-form-item>
        <el-form-item label="巡查频次" prop="frequency">
          <el-select v-model="form.frequency" placeholder="请选择巡查频次" style="width: 100%" @change="handleFrequencyChange">
            <el-option v-for="(item, index) in frequencyList" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="巡查时间" prop="date1" v-if="form.frequency === '0'">
          <el-select v-model="form.date1" placeholder="请选择月份" style="width: 100%">
            <el-option
              v-for="item in monthOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="巡查时间" prop="date1" v-if="form.frequency === '1'">
          <el-select v-model="form.date1" placeholder="请选择日" style="width: 100%">
            <el-option
              v-for="item in dayOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="巡查时间" prop="date1" v-if="form.frequency === '2'">
          <el-time-picker
            v-model="form.date1"
            placeholder="选择时间"
            style="width: 100%;"
            value-format="HH:mm:ss" />
        </el-form-item>
        <el-form-item label="通知方式" prop="notifyType">
          <el-radio-group v-model="form.notifyType">
            <el-radio label="0">微信</el-radio>
            <el-radio label="1">电话</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否发布" prop="delivery">
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
    },
    notifyTypeFilter(type) {
      const typeMap = {
        0: '微信',
        1: '电话'
      };
      return typeMap[type] || '未知';
    }
  },
  data() {
    return {
      list: [],
      listLoading: true,
      dialogVisible: false,
      title: '新增巡检计划',
      personList: [],
      frequencyList: [
        { label: '年', value: '0' },
        { label: '月', value: '1' },
        { label: '日', value: '2' }
      ],
      typeList: [
        { label: '消防安全', value: '0' },
        { label: '食堂安全', value: '1' },
        { label: '护工查房', value: '2' }
      ],
      monthOptions: Array.from({ length: 12 }, (_, i) => ({
        value: `每年${i + 1}月`,
        label: `${i + 1}月`
      })),
      dayOptions: Array.from({ length: 31 }, (_, i) => ({
        value: `每月${i + 1}日`,
        label: `${i + 1}日`
      })),
      form: {
        id: null,
        name: '',
        person: '',
        region: '',
        place: '',
        frequency: '',
        date1: null,
        notifyType: '0',
        delivery: false
      },
      rules: {
        name: [
          { required: true, message: '请输入巡检计划名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        person: [
          { required: true, message: '请选择巡查人', trigger: 'change' }
        ],
        region: [
          { required: true, message: '请选择类目', trigger: 'change' }
        ],
        place: [
          { required: true, message: '请输入巡查地点', trigger: 'blur' }
        ],
        frequency: [
          { required: true, message: '请选择巡查频次', trigger: 'change' }
        ],
        date1: [
          { required: true, message: '请选择巡查时间', trigger: 'change' }
        ],
        notifyType: [
          { required: true, message: '请选择通知方式', trigger: 'change' }
        ]
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchName: '',
      operateType: 'add'
    };
  },
  created() {
    this.fetchData();
    this.fetchUserList();
  },
  methods: {
    async fetchUserList() {
      try {
        // 首先获取用户列表
        const listResponse = await this.$http({
          url: this.$http.adornUrl('/sys/user/list'),
          method: 'get',
          params: this.$http.adornParams({
            page: 1,
            limit: 1000
          })
        });

        if (listResponse.data && listResponse.data.code === 0) {
          const users = listResponse.data.page.list;
          this.personList = [];
            console.log("yonghu1",users)
          for (const user of users) {
            if (!user.userId) continue;
            try {
              // 获取用户详情
              const infoResponse = await this.$http({
                url: this.$http.adornUrl(`/sys/user/info/${user.userId}`),
                method: 'get'
              });
              console.log("yonghu2",infoResponse.data.user.roleIdList)
              if (infoResponse.data && infoResponse.data.code === 0) {
                const userInfo = infoResponse.data.user;
                // 检查用户角色是否包含2
                if (userInfo.roleIdList && userInfo.roleIdList.includes(2)) {
                  this.personList.push({
                    label: user.username ,
                    value: user.userId.toString()
                  });
                }
              }
            } catch (error) {
              console.error(`获取用户${user.userId}详情失败:`, error);
            }
          }
        } else {
          this.$message.error(listResponse.data.msg || '获取用户列表失败');
        }
      } catch (error) {
        console.error('获取用户列表失败:', error);
        this.$message.error('获取用户列表失败');
      }
    },
    
    fetchData() {
      this.listLoading = true;
      const params = {
        pageNo: this.currentPage,
        pageSize: this.pageSize,
        name: this.searchName
      };

      this.$http({
        url: this.$http.adornUrl('/inspection/patrolPlan/list'),
        method: 'get',
        params: this.$http.adornParams(params)
      }).then(({ data }) => {
        console.log('data', data);
        if (data.code === 0) {
          this.list = data.page.records;
          this.total = data.page.total;
        }
        this.listLoading = false;
      }).catch((error) => {
        console.error('请求失败:', error);
        this.listLoading = false;
        this.$message.error('获取数据失败');
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
          frequency: '',
          date1: null,
          notifyType: '0',
          delivery: false
        };
      } else if (type === 'edit') {
        this.title = '修改巡检计划';
        const frequency = this.getFrequencyValue(row.displayTime);
        this.form = {
          id: row.id,
          name: row.name,
          person: this.getPersonValue(row.author),
          region: this.getTypeValue(row.type),
          place: row.place,
          frequency: frequency,
          date1: this.parseDisplayTime(row.displayTime, frequency),
          notifyType: row.notifyType || '0',
          delivery: row.status === 0
        };
      }
      this.dialogVisible = true;
    },

    parseDisplayTime(displayTime, frequency) {
      if (!displayTime) return null;
      
      try {
        if (frequency === '0') {
          // 年频次 - 提取月份部分 (格式: MM)
          const month = displayTime.split('-')[1];
          return month || '01';
        } else if (frequency === '1') {
          // 月频次 - 提取日部分 (格式: dd)
          const day = displayTime.split(' ')[0].split('-')[2];
          return day || '01';
        } else {
          // 日频次 - 提取时间部分 (格式: HH:mm:ss)
          const time = displayTime.split(' ')[1];
          return time || '00:00:00';
        }
      } catch (e) {
        console.error('解析时间失败:', e);
        return null;
      }
    },

    onSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return false;
        }

        if (!this.form.date1) {
          this.$message.error('请选择巡查时间');
          return;
        }

        const requestData = {
          id: this.form.id,
          name: this.form.name,
          author: this.getPersonLabel(this.form.person),
          type: this.getTypeLabel(this.form.region),
          place: this.form.place,
          displayTime: this.formatDisplayTime(),
          notifyType: this.form.notifyType,
          status: this.form.delivery ? 0 : 1
        };

        let url = '';
        let method = '';
        if (this.operateType === 'add') {
          url = '/inspection/patrolPlan/add';
          method = 'post';
        } else {
          url = '/inspection/patrolPlan/edit';
          method = 'put';
        }

        this.$http({
          url: this.$http.adornUrl(url),
          method: method,
          data: this.$http.adornData(requestData)
        }).then(({ data }) => {
          if (data.code === 0) {
            this.$message.success(data.msg);
            this.dialogVisible = false;
            this.fetchData();
          } else {
            this.$message.error(data.msg);
          }
        }).catch((error) => {
          console.error('操作失败:', error);
          this.$message.error('操作失败，请稍后重试');
        });
      });
    },

    formatDisplayTime() {
        return this.form.date1; 
    },

    getFrequencyValue(displayTime) {
      if (!displayTime) return '2';
      
      // 根据显示时间的格式判断频次
      if (displayTime.match(/^\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/)) {
        return '0'; // 年频次 (MM-dd HH:mm:ss)
      } else if (displayTime.match(/^\d{2} \d{2}:\d{2}:\d{2}$/)) {
        return '1'; // 月频次 (dd HH:mm:ss)
      } else {
        return '2'; // 日频次 (HH:mm:ss)
      }
    },

    getPersonLabel(value) {
      const person = this.personList.find(item => item.value === value);
      return person ? person.label : value;
    },

    getTypeLabel(value) {
      const type = this.typeList.find(item => item.value === value);
      return type ? type.label : '';
    },

    getPersonValue(label) {
      const person = this.personList.find(item => item.label === label);
      return person ? person.value : label;
    },

    getTypeValue(label) {
      const type = this.typeList.find(item => item.label === label);
      return type ? type.value : '';
    },

    handleFrequencyChange(value) {
      this.form.date1 = null;
    },

    onCancel() {
      this.dialogVisible = false;
      this.$refs.form.resetFields();
    },

    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.fetchData();
    },

    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.fetchData();
    },

    deleteItem(id) {
      this.$confirm('确定要删除该记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/inspection/patrolPlan/delete'),
          method: 'delete',
          params: this.$http.adornParams({ id })
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
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    pauseItem(id) {
      this.updateStatus(id, 2);
    },

    updateStatus(id, status) {
      this.$http({
        url: this.$http.adornUrl('/inspection/inspectionTask/edit'),
        method: 'put',
        params: this.$http.adornParams({ id, status })
      }).then(({ data }) => {
        if (data.code === 0) {
          this.$message.success('操作成功');
          this.fetchData();
        } else {
          this.$message.error('操作失败');
        }
      }).catch((error) => {
        console.error('操作失败:', error);
        this.$message.error('操作失败');
      });
    },

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
.search-container {
  margin-bottom: 20px;
}
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>