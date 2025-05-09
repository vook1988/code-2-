<template>
    <div class="label-manage">
        <h2>标签管理</h2>
        <el-button type="primary" @click="openAddLabelDialog">新增标签</el-button>
        <el-table :data="labels">
            <el-table-column prop="labelName" label="标签名称" />
            <el-table-column label="颜色">
                <template #default="{ row }">
                    <div class="color-block" :style="{ backgroundColor: row.labelColor }"></div>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="{ row }">
                    <el-button size="small" @click="editLabel(row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteLabel(row)">删除</el-button>
                    <el-button size="small" type="primary" @click="viewLabelAnnotations(row)">查看标注</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 新增/编辑标签对话框 -->
        <el-dialog v-model="addLabelDialogVisible" title="标签操作">
            <el-form :model="currentLabel" label-width="80px">
                <el-form-item label="标签名称">
                    <el-input v-model="currentLabel.labelName" required />
                </el-form-item>
                <el-form-item label="标签颜色">
                    <el-color-picker v-model="currentLabel.color" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="addLabelDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveLabel">保存</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

const labels = ref([]);
const addLabelDialogVisible = ref(false);
const currentLabel = ref({ labelName: '', color: '#FF5733' });
const currentUserId = ref(4);

// 加载标签列表（修正数据解析）
const fetchLabels = async () => {
    try {
        // 传递userId参数
        const res = await axios.get(`/api/label/list?userId=${currentUserId.value}`);
        labels.value = res.data.data || [];
    } catch (error) {
        console.error('获取标签列表失败:', error);
        ElMessage.error('获取标签列表失败，请稍后重试');
    }
};;
fetchLabels();

// 打开新增对话框
const openAddLabelDialog = () => {
    currentLabel.value = { labelName: '', color: '#FF5733' };
    addLabelDialogVisible.value = true;
};

// 保存标签（新增/编辑）
const saveLabel = async () => {
    if (!currentLabel.value.labelName) {
        ElMessage.error('标签名称不能为空');
        return;
    }
    const url = currentLabel.value.labelId
        ? 'http://localhost:8080/label/update'
        : 'http://localhost:8080/label/add';
    try {
        const res = await axios.post(url, {
            labelName: currentLabel.value.labelName,
            labelColor: currentLabel.value.color,
            labelId: currentLabel.value.labelId // 编辑时传递ID
        });
        if (res.data.code === 200) {
            ElMessage.success('保存成功');
            addLabelDialogVisible.value = false;
            fetchLabels(); // 刷新列表
        } else {
            ElMessage.error(res.data.msg);
        }
    } catch (error) {
        console.error('保存标签失败:', error);
        ElMessage.error('保存标签失败，请稍后重试');
    }
};

// 编辑标签
const editLabel = (label) => {
    currentLabel.value = {
        labelId: label.labelId,
        labelName: label.labelName,
        color: label.labelColor // 注意：前端用color字段绑定，后端用labelColor
    };
    addLabelDialogVisible.value = true;
};

// 删除标签
const deleteLabel = async (label) => {
    try {
        const res = await axios.delete(`http://localhost:8080/label/delete/${label.labelId}`);
        if (res.data.code === 200) {
            ElMessage.success('删除成功');
            fetchLabels();
        } else {
            ElMessage.error(res.data.msg);
        }
    } catch (error) {
        ElMessage.error('删除失败：' + error.response.data.msg);
    }
};
const downloadLabel = async (labelIds) => {
    const res = await axios.get(`/label/download?labelIds=${labelIds.join(',')}`, {
        responseType: 'blob' // 接收二进制流
    });

    // 保存文件到本地
    const blob = new Blob([res.data], { type: 'application/zip' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'labels.zip';
    link.click();
};
const viewLabelAnnotations = async (label) => {
    try {
        // 获取该标签关联的第一个图片ID（需根据实际业务调整）
        const res = await axios.get(`/api/annotation/getImageIdsByLabelId?labelId=${label.labelId}`);
        if (res.data.code !== 200 || res.data.data.length === 0) {
            ElMessage.warning('该标签无关联图片');
            return;
        }
        const imageId = res.data.data[0]; // 取第一个关联图片

        // 跳转到Annotate页，传递imageId和labelId
        router.push({
            path: '/annotate',
            query: { imageId: imageId, labelId: label.labelId }
        });
    } catch (error) {
        ElMessage.error(`获取标签关联图片失败: ${error.message}`);
    }
};
</script>

<style scoped>
.color-block {
    width: 30px;
    height: 20px;
    border: 1px solid #ddd;
}

.label-manage {
    min-height: 80vh;
    /* 占满视口高度的80% */
    padding: 20px;
}
</style>