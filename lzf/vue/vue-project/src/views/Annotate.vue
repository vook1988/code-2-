<template>
    <div class="container">
        <!-- 左侧 -->
        <div class="left">
            <!-- 图片 -->
            <div class="left-top" ref="wrapper">
                <canvas ref="canvas" @mousedown="handleMouseDown" @mousemove="handleMouseMove" @mouseup="handleMouseUp"
                    @dblclick="handleDoubleClick"></canvas>
                <!-- <img v-if="currentImage && currentImage.storagePath" :src="getImageUrl(currentImage.storagePath)"
                    alt="标注图像" @load="handleImageLoad"> -->
                <!-- 显示鼠标位置 -->
                <span v-if="creating || creatingPolygon" class="mouse-position">
                    鼠标位置 x: {{ currentX }}, y: {{ currentY }}
                </span>
            </div>
            <!-- 工具栏 -->
            <div class="left-bottom">
                <el-tooltip effect="light" content="上一张" placement="top">
                    <el-button circle type="warning" @click="handleImageSwitch(currentImageIndex - 1)">
                        <el-icon>
                            <CaretLeft />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="新建标记" placement="top">
                    <el-button circle type="primary" @click="toggleCreate('rect')" :disabled="false">
                        <el-icon>
                            <Crop />
                        </el-icon>

                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="新建多边形标记" placement="top">
                    <el-button circle type="primary" @click="toggleCreate('polygon')" :disabled="false">
                        <el-icon>
                            <Crop />
                        </el-icon>

                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="撤销多边形点" placement="top">
                    <el-button circle type="danger" @click="undoPolygonPoint"
                        :disabled="!creatingPolygon || currentPolygon.length === 0">
                        <el-icon>
                            <Fold />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="保存标记" placement="top">
                    <el-button circle type="success" @click="handleSaveLabel">
                        <el-icon>
                            <Folder />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="删除标记" placement="top">
                    <el-button circle type="danger" @click="handleDeleteLabel"
                        :disabled="selectedRectIndex === -1 && selectedPolygonIndex === -1">
                        <el-icon>
                            <FolderDelete />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tag type="plain" style="margin: 0 10px">
                    缩放率: {{ (Math.round(scale * 100) / 100).toFixed(2) }}
                </el-tag>
                <el-tooltip effect="light" content="放大" placement="top">
                    <el-button circle type="primary" @click="zoomIn" :disabled="scale >= maxScale">
                        <el-icon>
                            <ZoomIn />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="缩小" placement="top">
                    <el-button circle type="primary" @click="zoomOut" :disabled="scale <= minScale">
                        <el-icon>
                            <ZoomOut />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="自适应" placement="top">
                    <el-button circle type="primary" @click="zoomToFit">
                        <el-icon>
                            <FullScreen />
                        </el-icon>
                    </el-button>
                </el-tooltip>
                <el-tooltip effect="light" content="下一张" placement="top">
                    <el-button circle type="warning" @click="handleImageSwitch(currentImageIndex + 1)">
                        <el-icon>
                            <CaretRight />
                        </el-icon>
                    </el-button>
                </el-tooltip>
            </div>
        </div>
        <!-- 右侧信息展示栏 -->
        <div class="right">
            <el-card class="label-list">
                <template #header>
                    <span style="font-size: 16px; font-weight: bold;">标签列表</span>
                </template>
                <el-table :data="labels">
                    <el-table-column prop="labelName" label="标签名称"></el-table-column>
                    <el-table-column label="操作">
                        <template #default="{ row }">
                            <el-button size="small" @click="viewLabelAnnotations(row.labelId)">查看标注</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-dialog :visible.sync="showAnnotationsDialog">
                    <el-table :data="currentAnnotations">
                        <el-table-column prop="annotationType" label="标注类型"></el-table-column>
                        <el-table-column label="坐标">
                            <template #default="{ row }">
                                {{ row.coordinates }}
                            </template>
                        </el-table-column>
                    </el-table>
                </el-dialog>
            </el-card>
            <el-card class="image-list">
                <template #header>
                    <span style="font-size: 16px; font-weight: bold;">图片列表</span>
                </template>
                <el-table fit :show-header="false" :data="imageList" @cell-click="imageCellClick" style="width: 100%">
                    <el-table-column prop="filename" label="图片名称" width="200"></el-table-column>
                </el-table>
            </el-card>
        </div>
        <!-- 标签命名弹窗 -->
        <el-dialog width="17vw" title="标签命名" :modal="false" :before-close="handleCancelInput"
            :close-on-click-modal="false" v-model="showNameInput">
            <el-form ref="tag">
                <el-form-item>
                    <el-select v-model="labelName" filterable allow-create clearable default-first-option
                        placeholder="请输入或选择已有标签名">
                        <el-option v-for="(item, i) in uniqueName" :key="i" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" @click="handleInputLabel" type="primary">确认</el-button>
                    <el-button size="mini" @click="handleCancelInput">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 提示保存弹窗 -->
        <el-dialog width="15vw" title="是否保存改动？" :modal="false" :close-on-click-modal="false" v-model="showSaveAlert">
            <el-form ref="tag">
                <el-form-item>
                    <el-button size="mini" type="success" @click="handleSaveChange(true)">是</el-button>
                    <el-button size="mini" type="warning" @click="handleSaveChange(false)">否</el-button>
                    <el-button size="mini" type="default" @click="showSaveAlert = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>

</template>

<script setup>
import axios from 'axios';
import { ref, onMounted, onUnmounted, computed, onBeforeUnmount, nextTick } from 'vue';
import Rect from '@/models/Rect.js'; // 导入类
import { ElMessage } from 'element-plus';
import { CaretLeft, Crop, Folder, FolderDelete, ZoomIn, ZoomOut, FullScreen, Fold } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router';

// 数据状态
const route = useRoute();
const imageId = ref(route.query.imageId || '');
const imageList = ref([]);
const labels = ref([]);
const images = ref([]);
const currentAnnotations = ref([]); // 用于标注对话框
const showAnnotationsDialog = ref(false);


// DOM 引用（唯一声明）
const canvas = ref(null);
const wrapper = ref(null);
const isMounted = ref(true);

// 其他状态变量
const creating = ref(false);
const creatingPolygon = ref(false);
const canvasChanged = ref(false);
const showNameInput = ref(false);
const showSaveAlert = ref(false);
const dpr = ref(1);
const scale = ref(0);
const maxScale = ref(3.0);
const minScale = ref(0.1);
const scaleStep = ref(0.1);
const prevX = ref(0);
const prevY = ref(0);
const currentX = ref(0);
const currentY = ref(0);
const currentImage = ref(null);
const currentImageIndex = ref(0);
const targetImageIndex = ref(-1);
const bufferCanvas = ref(null);
const currentRect = ref(null);
const currentPolygon = ref([]);
const selectedRect = ref(null);
const selectedRectIndex = ref(-1);
const selectedPolygon = ref(null);
const selectedPolygonIndex = ref(-1);
const labelName = ref("");
const rects = ref([]);
const polygons = ref([]);

const offsetX = ref(0);
const offsetY = ref(0);
const isDragging = ref(false);

// 计算属性
const imagePath = computed(() => {
    return currentImage.value?.storagePath || ''; // 直接使用 currentImage
});
const uniqueName = computed(() => {
    let names = new Set();
    rects.value.forEach(item => {
        names.add(item.name);
    });
    polygons.value.forEach(item => {
        names.add(item.name);
    });
    return Array.from(names);
});

// 初始化
const init = async () => {
    dpr.value = window.devicePixelRatio || 1;
    canvas.value = canvas.value;
    wrapper.value = wrapper.value;
    bufferCanvas.value = document.createElement('canvas');
    try {
        // 用于获取图片列表
        // 这里接口可能有误，确认是否是获取图片列表接口
        const res = await axios.get(`/api/image/list`);
        images.value = res.data.data;
        if (images.value.length === 0) {
            ElMessage.warning('图片列表为空，请检查数据源');
            return;
        }
        // 加载第一张图片
        currentImageIndex.value = 0;
        imageId.value = images.value[0].imageId;
        loadRects();
        loadPolygons();
        loadImage();
    } catch (error) {
        ElMessage.error(`获取图片列表失败: ${error.message}`);
    }
};

// 初始化时调用
onMounted(async () => {
    try {
        await nextTick();

        // 初始化设备像素比和画布
        dpr.value = window.devicePixelRatio || 1;
        bufferCanvas.value = document.createElement('canvas');

        // 获取图片列表
        await fetchImageList();

        // 从路由参数获取初始图片ID
        if (route.query.imageId) {
            imageId.value = route.query.imageId;
            await loadImage();

            // 如果有标签ID，加载对应标签的标注
            if (route.query.labelId) {
                await viewLabelAnnotations(route.query.labelId);
            }
        }

        // 获取标签列表
        await fetchLabels();
    } catch (error) {
        console.error('组件初始化失败:', error);
        ElMessage.error('初始化失败: ' + error.message);
    }
});
// 加载已保存矩形标签
const loadRects = () => {
    const storedRects = JSON.parse(sessionStorage.getItem(imageId.value + '_rects'));
    if (storedRects !== null) {
        const bufferCtx = bufferCanvas.value.getContext('2d');
        storedRects.forEach(item => {
            let rect = new Rect(bufferCtx, item.dpr, item.minX, item.minY, item.scale);
            rect.changed = false;
            rect.name = item.name;
            rect.maxX = item.maxX;
            rect.maxY = item.maxY;
            rect.timestamp = item.timestamp;
            rect.realScale = item.realScale;
            rects.value.push(rect);
        });
    }
};

// 加载已保存多边形标签
const loadPolygons = () => {
    const storedPolygons = JSON.parse(sessionStorage.getItem(imageId.value + '_polygons'));
    if (storedPolygons !== null) {
        storedPolygons.forEach(item => {
            const polygon = {
                points: item.points,
                name: item.name,
                color: 'rgba(0, 0, 255, 0.3)',
                timestamp: item.timestamp
            };
            polygons.value.push(polygon);
        });
    }
};


// 设置画布大小
const setSize = () => {
    // 增强检查，确保 DOM 元素已加载
    if (!canvas.value || !wrapper.value || !currentImage.value) {
        console.error('DOM 元素或当前图片未正确加载');
        return;
    }

    // 获取容器尺寸
    const container = wrapper.value.getBoundingClientRect();
    const scaledWidth = currentImage.value.width * scale.value;
    const scaledHeight = currentImage.value.height * scale.value;

    // 设置画布物理尺寸（考虑设备像素比）
    canvas.value.width = scaledWidth * dpr.value;
    canvas.value.height = scaledHeight * dpr.value;

    // 设置画布显示尺寸
    canvas.value.style.width = `${scaledWidth}px`;
    canvas.value.style.height = `${scaledHeight}px`;

    // 缓冲画布同步尺寸
    if (bufferCanvas.value) {
        bufferCanvas.value.width = scaledWidth * dpr.value;
        bufferCanvas.value.height = scaledHeight * dpr.value;
        bufferCanvas.value.style.width = `${scaledWidth}px`;
        bufferCanvas.value.style.height = `${scaledHeight}px`;
    }

    // 居中画布
    wrapper.value.style.justifyContent = 'center';
    wrapper.value.style.alignItems = 'center';
};

// 加载图片到画布
const loadImage = async () => {
    if (!imageId.value) {
        console.error('imageId 为空，无法加载图片');
        return;
    }

    try {
        const res = await axios.get(`/api/image/${imageId.value}`);
        const imgMeta = res.data.data;

        if (!imgMeta) {
            ElMessage.error(`图片元数据不存在，ID: ${imageId.value}`);
            return;
        }

        const img = new Image();
        img.src = getImageUrl(imgMeta.storagePath);

        img.onload = () => {
            currentImage.value = {
                ...imgMeta,
                width: img.naturalWidth,
                height: img.naturalHeight,
                imageObj: img
            };

            // 计算初始缩放比例
            calculateAdaptiveScale();

            // 使用 nextTick 确保 DOM 更新完成
            nextTick(() => {
                if (canvas.value && wrapper.value) {
                    setSize();
                    drawCanvas();
                } else {
                    console.error('DOM 元素未正确加载，无法设置尺寸');

                    // 尝试延迟重试
                    setTimeout(() => {
                        if (canvas.value && wrapper.value) {
                            setSize();
                            drawCanvas();
                        }
                    }, 300);
                }
            });
        };

        img.onerror = (error) => {
            console.error('图片加载失败:', error);
            ElMessage.error(`图片加载失败: ${error.message}`);
        };
    } catch (error) {
        console.error('获取图片信息失败:', error);
        ElMessage.error(`获取图片信息失败: ${error.message}`);
    }
};
//计算自适应缩放比例
const calculateAdaptiveScale = () => {
    if (!currentImage.value || !wrapper.value) return;

    const container = wrapper.value.getBoundingClientRect();
    const scaleX = container.width / currentImage.value.width;
    const scaleY = container.height / currentImage.value.height;
    scale.value = Math.min(scaleX, scaleY, 1.0); // 限制最大缩放为 100%
};


// 添加图片路径处理方法
const getImageUrl = (storagePath) => {
    if (!storagePath) {
        console.error('Invalid storagePath:', storagePath);
        return '';
    }
    return `http://localhost:8080/${storagePath}`;
};
// 绘制画布
const drawCanvas = () => {
    const ctx = canvas.value.getContext('2d');
    const bufferCtx = bufferCanvas.value.getContext('2d');
    const width = canvas.value.width;
    const height = canvas.value.height;
    // 检查 currentImage.value 是否存在
    if (currentImage.value && currentImage.value.imageObj) {
        bufferCtx.clearRect(0, 0, width, height);
        ctx.clearRect(0, 0, width, height);
        // 绘制图片到画布，应用偏移量
        bufferCtx.drawImage(
            currentImage.value.imageObj,
            offsetX.value,
            offsetY.value,
            currentImage.value.width * scale.value,
            currentImage.value.height * scale.value
        );
    }
    if (currentRect.value) {
        currentRect.value.draw(scale.value);
        drawCrosshair(ctx, currentRect.value.minX, currentRect.value.minY);
        drawCrosshair(ctx, currentRect.value.maxX, currentRect.value.minY);
        drawCrosshair(ctx, currentRect.value.minX, currentRect.value.maxY);
        drawCrosshair(ctx, currentRect.value.maxX, currentRect.value.maxY);
    }
    if (currentPolygon.value.length > 0) {
        drawPolygonLines(bufferCtx, currentPolygon.value, 'rgba(0, 0, 255, 0.3)');
    }
    for (const rect of rects.value) {
        if (rect === selectedRect.value) {
            rect.color = 'rgba(255, 0, 0, 0.3)';
        } else {
            rect.color = 'rgba(0, 0, 255, 0.3)';
        }
        rect.draw(scale.value);
        drawCrosshair(ctx, rect.minX, rect.minY);
        drawCrosshair(ctx, rect.maxX, rect.minY);
        drawCrosshair(ctx, rect.minX, rect.maxY);
        drawCrosshair(ctx, rect.maxX, rect.maxY);
    }
    for (const polygon of polygons.value) {
        if (polygon === selectedPolygon.value) {
            polygon.color = 'rgba(255, 0, 0, 0.3)';
        } else {
            polygon.color = 'rgba(0, 0, 255, 0.3)';
        }
        drawPolygon(bufferCtx, polygon.points, polygon.color);
    }
    ctx.drawImage(bufferCanvas.value, 0, 0, width, height);
};
// 绘制多边形线段
const drawPolygonLines = (ctx, points, color) => {
    if (points.length === 0) return;

    ctx.beginPath();
    // 遍历所有点，按顺序绘制连接线段（修正：使用完整points数组）
    points.forEach((point, index) => {
        // 转换为画布显示坐标（考虑缩放和偏移）
        const displayX = point.x * scale.value + offsetX.value / dpr.value;
        const displayY = point.y * scale.value + offsetY.value / dpr.value;

        if (index === 0) {
            ctx.moveTo(displayX, displayY);  // 第一个点作为起点
        } else {
            ctx.lineTo(displayX, displayY);  // 后续点依次连接
        }
    });

    // 绘制当前鼠标到最后一个点的临时线段（新增）
    if (creatingPolygon.value && points.length > 0) {
        const lastPoint = points[points.length - 1];
        const lastDisplayX = lastPoint.x * scale.value + offsetX.value / dpr.value;
        const lastDisplayY = lastPoint.y * scale.value + offsetY.value / dpr.value;
        const currentDisplayX = currentX.value * dpr.value;  // 当前鼠标在画布的物理坐标
        const currentDisplayY = currentY.value * dpr.value;
        ctx.lineTo(currentDisplayX, currentDisplayY);  // 连接最后一个点和当前鼠标位置
    }
    // 绘制设置
    ctx.strokeStyle = color;
    ctx.lineWidth = 3;
    ctx.stroke();

    // 绘制点
    ctx.fillStyle = 'red';
    points.forEach(point => {
        const displayX = point.x * scale.value + offsetX.value / dpr.value;
        const displayY = point.y * scale.value + offsetY.value / dpr.value;
        ctx.beginPath();
        ctx.arc(displayX, displayY, 5, 0, 2 * Math.PI);
        ctx.fill();
    });
};
// 绘制多边形
const drawPolygon = (ctx, points, color) => {
    ctx.beginPath();
    points.forEach((point, index) => {
        const displayX = point.x * scale.value + offsetX.value / dpr.value;
        const displayY = point.y * scale.value + offsetY.value / dpr.value;
        if (index === 0) {
            ctx.moveTo(displayX, displayY);
        } else {
            ctx.lineTo(displayX, displayY);
        }
    });
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 3;
    ctx.stroke();
    // 绘制每个点
    ctx.fillStyle = 'red';
    points.forEach(point => {
        const displayX = point.x * scale.value + offsetX.value / dpr.value;
        const displayY = point.y * scale.value + offsetY.value / dpr.value;
        ctx.beginPath();
        ctx.arc(displayX, displayY, 5, 0, 2 * Math.PI);
        ctx.fill();
    });
};
// 绘制十字线
const drawCrosshair = (ctx, x, y) => {
    ctx.beginPath();
    ctx.moveTo(x * scale.value - 5, y * scale.value);
    ctx.lineTo(x * scale.value + 5, y * scale.value);
    ctx.moveTo(x * scale.value, y * scale.value - 5);
    ctx.lineTo(x * scale.value, y * scale.value + 5);
    ctx.strokeStyle = 'red';
    ctx.stroke();
};

// 鼠标按下
const handleMouseDown = (e) => {
    const rect = canvas.value.getBoundingClientRect();

    // 新的坐标转换方法
    const getImageCoord = (clientX, clientY) => {
        const canvasRect = canvas.value.getBoundingClientRect();

        // 1. 计算鼠标在canvas元素上的物理坐标（考虑dpr）
        const canvasX = (clientX - canvasRect.left) * dpr.value;
        const canvasY = (clientY - canvasRect.top) * dpr.value;

        // 2. 减去画布偏移量（拖动后的位置），得到相对于图像左上角的物理坐标
        const imagePhysicalX = canvasX - offsetX.value;
        const imagePhysicalY = canvasY - offsetY.value;

        // 3. 转换为原始图像的实际坐标（除以缩放比例）
        const imageRealX = imagePhysicalX / scale.value;
        const imageRealY = imagePhysicalY / scale.value;

        return { x: imageRealX, y: imageRealY };
    };
    const { x: mouseX, y: mouseY } = getImageCoord(e.clientX, e.clientY);

    prevX.value = mouseX;
    prevY.value = mouseY;
    selectedRect.value = null;
    selectedRectIndex.value = -1;
    selectedPolygon.value = null;
    selectedPolygonIndex.value = -1;
    for (let i = rects.value.length - 1; i > -1; i--) {
        const rect = rects.value[i];
        if (rect.isSelected(mouseX * scale.value / dpr.value, mouseY * scale.value / dpr.value)) {
            selectedRect.value = rect;
            selectedRectIndex.value = i;
            break;
        }
    }
    for (let i = polygons.value.length - 1; i > -1; i--) {
        const polygon = polygons.value[i];
        if (isPointInPolygon(mouseX * scale.value / dpr.value, mouseY * scale.value / dpr.value, polygon.points)) {
            selectedPolygon.value = polygon;
            selectedPolygonIndex.value = i;
            break;
        }
    }
    if (creating.value) {
        const bufferCtx = bufferCanvas.value.getContext('2d');
        currentRect.value = new Rect(bufferCtx, dpr.value, mouseX * scale.value / dpr.value, mouseY * scale.value / dpr.value, scale.value);
    }
    else if (creatingPolygon.value) {
        // 添加调试日志
        console.log('Canvas Rect:', rect);
        console.log('Converted Coordinates:', { x: mouseX, y: mouseY });
        if (currentPolygon.value.length > 0) {
            const startPoint = currentPolygon.value[0];
            // 起始点在画布上的显示坐标（考虑缩放和偏移）
            const startDisplayX = startPoint.x * scale.value + offsetX.value / dpr.value;
            const startDisplayY = startPoint.y * scale.value + offsetY.value / dpr.value;
            // 当前鼠标在画布上的物理坐标（已考虑dpr）
            const currentDisplayX = (e.clientX - rect.left) * dpr.value;
            const currentDisplayY = (e.clientY - rect.top) * dpr.value;

            // 计算两点间距离（像素）
            const distance = Math.sqrt(
                Math.pow(currentDisplayX - startDisplayX, 2) +
                Math.pow(currentDisplayY - startDisplayY, 2)
            );

            if (distance < 15) {  // 增大容差范围（原10像素可能过小）
                showNameInput.value = true;  // 触发闭合并保存
                currentPolygon.value.push(startPoint);  // 自动闭合多边形
                return;
            }
        }

        // 添加新点（保留原有逻辑）
        currentPolygon.value.push({ x: mouseX, y: mouseY });
    } else if (selectedRect.value) {
        selectedRect.value.mouseDown(mouseX * scale.value / dpr.value, mouseY * scale.value / dpr.value);
    } else {
        // 开始拖动
        isDragging.value = true;
        prevX.value = e.clientX;
        prevY.value = e.clientY;
    }
    drawCanvas();
};
// 判断点是否在多边形内
const isPointInPolygon = (x, y, polygon) => {
    let inside = false;
    for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
        const xi = polygon[i].x * scale.value, yi = polygon[i].y * scale.value;
        const xj = polygon[j].x * scale.value, yj = polygon[j].y * scale.value;

        const intersect = ((yi > y) !== (yj > y)) &&
            (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }
    return inside;
};

// 鼠标移动
const handleMouseMove = (e) => {
    const mouseX = e.offsetX;
    const mouseY = e.offsetY;
    currentX.value = mouseX;
    currentY.value = mouseY;
    const ctx = canvas.value.getContext('2d');
    if (creating.value) {
        ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
        ctx.drawImage(bufferCanvas.value, 0, 0);
        ctx.beginPath();
        ctx.moveTo(mouseX * dpr.value, 0);
        ctx.lineTo(mouseX * dpr.value, canvas.value.height);
        ctx.moveTo(0, mouseY * dpr.value);
        ctx.lineTo(canvas.value.width, mouseY * dpr.value);
        ctx.strokeStyle = 'red';
        ctx.stroke();
        if (!currentRect.value) return;
        currentRect.value.maxX = mouseX;
        currentRect.value.maxY = mouseY;
    } else if (creatingPolygon.value && currentPolygon.value.length > 0) {
        const lastPoint = currentPolygon.value[currentPolygon.value.length - 1];
        // 计算最后一个点的显示坐标（考虑缩放和偏移）
        const lastDisplayX = lastPoint.x * scale.value + offsetX.value / dpr.value;
        const lastDisplayY = lastPoint.y * scale.value + offsetY.value / dpr.value;

        // 计算当前鼠标的显示坐标（使用getImageCoord转换后的实际坐标）
        const { x: currentRealX, y: currentRealY } = getImageCoord(e.clientX, e.clientY);
        const currentDisplayX = currentRealX * scale.value + offsetX.value / dpr.value;
        const currentDisplayY = currentRealY * scale.value + offsetY.value / dpr.value;


        ctx.clearRect(0, 0, canvas.value.width, canvas.value.height);
        ctx.drawImage(bufferCanvas.value, 0, 0);
        drawPolygonLines(ctx, currentPolygon.value, 'rgba(0, 0, 255, 0.3)');
        ctx.beginPath();
        ctx.moveTo(lastDisplayX, lastDisplayY);
        ctx.lineTo(currentDisplayX, currentDisplayY);  // 使用转换后的坐标
        ctx.strokeStyle = 'red';
        ctx.stroke();
    } else if (selectedRect.value) {
        selectedRect.value.mouseMove(e, { currentImage: currentImage.value, scale: scale.value });
    } else if (isDragging.value) {
        const dx = e.clientX - prevX.value;
        const dy = e.clientY - prevY.value;
        offsetX.value += dx;
        offsetY.value += dy;
        prevX.value = e.clientX;
        prevY.value = e.clientY;
        drawCanvas();
    }
    if (creating.value || creatingPolygon.value || selectedRect.value) {
        drawCanvas();
    }
};
// 鼠标抬起
const handleMouseUp = (e) => {
    if (creating.value) {
        currentRect.value.maxX = e.offsetX;
        currentRect.value.maxY = e.offsetY;
        creating.value = false;
        if (currentRect.value.minX !== currentRect.value.maxX
            && currentRect.value.minY !== currentRect.value.maxY) {
            showNameInput.value = true;
        }
    } else if (creatingPolygon.value) {
        // 单击不做处理，继续标注
    } else if (selectedRect.value) {
        selectedRect.value.mouseUp(currentImage.value.width, currentImage.value.height);
    }
    isDragging.value = false; // 结束拖动
    drawCanvas();
};

// 双击事件处理
const handleDoubleClick = (e) => {
    if (creatingPolygon.value && currentPolygon.value.length >= 3) {
        // 自动闭合时添加起点坐标
        currentPolygon.value.push({ ...currentPolygon.value[0] });
        showNameInput.value = true;
        drawCanvas();
    }
};

const toggleCreate = (type) => {
    if (type === 'rect') {
        // 切换状态时清理临时数据
        if (creating.value) {  // 取消创建时
            currentRect.value = null;  // 清空当前矩形
        }
        creating.value = !creating.value;
    } else if (type === 'polygon') {
        if (creatingPolygon.value) {  // 取消创建时
            currentPolygon.value = [];  // 清空当前多边形顶点
        }
        creatingPolygon.value = !creatingPolygon.value;
    }
    drawCanvas();  // 切换后重绘画布
};




//坐标系转换工具
const getCanvasCoordinates = (e) => {
    const rect = canvas.value.getBoundingClientRect();
    return {
        x: (e.clientX - rect.left) * dpr.value / scale.value,
        y: (e.clientY - rect.top) * dpr.value / scale.value
    };
};

// 输入标签保存标签
const handleInputLabel = async () => {
    if (!labelName.value) {
        ElMessage.error('标签名称不能为空');
        return;
    }

    const currentUserId = 4; // 实际应从用户会话获取
    let labelId;

    try {
        // 1. 查询是否存在同名标签（当前用户创建）
        const labelRes = await axios.get(`/label/getByName?labelName=${labelName.value}&userId=${currentUserId}`);
        if (labelRes.data.code === 200 && labelRes.data.data) {
            labelId = labelRes.data.data.labelId;
        } else {
            // 2. 不存在则创建新标签
            const createRes = await axios.post('/api/label/add', {
                labelName: labelName.value,
                labelColor: '#FF5733', // 可前端增加颜色选择器
                createdBy: currentUserId
            });
            if (createRes.data.code !== 200) throw new Error('标签创建失败');
            labelId = createRes.data.data.labelId;
        }

        // 3. 构造标注数据
        let annotationType = '';
        if (currentRect.value) {
            annotationType = 'rect';
        } else if (currentPolygon.value.length > 2) {
            annotationType = 'polygon';
        }

        const annotationData = {
            imageId: imageId.value,
            labelId: labelId,
            createdBy: currentUserId,
            annotationType: annotationType
        };

        // 4. 处理坐标数据
        if (currentRect.value) {
            const { minX, minY, maxX, maxY } = currentRect.value;
            annotationData.coordinates = `[[${minX},${minY}], [${maxX},${maxY}]]`;
        } else if (currentPolygon.value.length > 2) {
            const points = currentPolygon.value.map(p => `[${p.x},${p.y}]`).join(',');
            annotationData.coordinates = `[${points}]`;
        } else {
            ElMessage.error('无效的标注区域');
            return;
        }

        // 5. 调用后端保存接口
        const saveRes = await axios.post('/api/annotation/save', annotationData);
        if (saveRes.data.code === 200) {
            ElMessage.success('标注保存成功');
            // 更新本地状态
            if (currentRect.value) rects.value.push(currentRect.value);
            if (currentPolygon.value.length > 2) polygons.value.push({
                points: currentPolygon.value,
                name: labelName.value,
                labelId: labelId
            });
            canvasChanged.value = true;
        } else {
            ElMessage.error('标注保存失败：' + saveRes.data.msg);
        }
    } catch (error) {
        ElMessage.error('操作失败：' + error.message);
    }

    // 清理临时数据
    currentRect.value = null;
    currentPolygon.value = [];
    showNameInput.value = false;
    drawCanvas();
};



// 取消输入
const handleCancelInput = () => {
    currentRect.value = null;
    currentPolygon.value = [];
    showNameInput.value = false;
    drawCanvas();
};

// 保存标签
const handleSaveLabel = () => {
    if (canvasChanged.value) {
        rects.value.forEach(item => {
            if (item.changed) {
                item.changed = false;
                const { x, y, w, h } = item.normalize(currentImage.value.width, currentImage.value.height);
                console.log("归一化后的参数：", `x: ${x}, y: ${y}, h: ${w}, h: ${h}`);
            }
        });
        polygons.value.forEach(item => {
            const normalizedPoints = item.points.map(point => ({
                x: point.x / currentImage.value.width,
                y: point.y / currentImage.value.height
            }));
            console.log("归一化后的多边形点：", normalizedPoints);
        });
        canvasChanged.value = false;
    }
    sessionStorage.setItem(images.value[currentImageIndex.value].id + '_rects', JSON.stringify(rects.value));
    sessionStorage.setItem(images.value[currentImageIndex.value].id + '_polygons', JSON.stringify(polygons.value));
    alert('保存成功');
};

// 删除选中标签
const handleDeleteLabel = (row) => {
    if (row instanceof Rect) {
        for (let i = 0; i < rects.value.length; i++) {
            if (row.timestamp === rects.value[i].timestamp) {
                selectedRectIndex.value = i;
                break;
            }
        }
        rects.value.splice(selectedRectIndex.value, 1);
        selectedRectIndex.value = -1;
    } else {
        for (let i = 0; i < polygons.value.length; i++) {
            if (row.timestamp === polygons.value[i].timestamp) {
                selectedPolygonIndex.value = i;
                break;
            }
        }
        polygons.value.splice(selectedPolygonIndex.value, 1);
        selectedPolygonIndex.value = -1;
    }
    canvasChanged.value = true;
    alert('删除成功');
    drawCanvas();
};

// 标签发生变化
const handleSaveChange = (flag) => {
    showSaveAlert.value = false;
    if (flag) {
        sessionStorage.setItem(images.value[currentImageIndex.value].id + '_rects', JSON.stringify(rects.value));
        sessionStorage.setItem(images.value[currentImageIndex.value].id + '_polygons', JSON.stringify(polygons.value));
        handleSaveLabel();
    }
    executeSwitch();
};

// 图片缩放
const zoomIn = () => {
    const newScale = scale.value + scaleStep.value;
    if (newScale <= maxScale.value) {
        const scaleFactor = newScale / scale.value;
        offsetX.value *= scaleFactor;
        offsetY.value *= scaleFactor;
        scale.value = newScale;
        setSize();
        drawCanvas();
    }
};

const zoomOut = () => {
    const newScale = scale.value - scaleStep.value;
    if (newScale >= minScale.value) {
        const scaleFactor = newScale / scale.value;
        offsetX.value *= scaleFactor;
        offsetY.value *= scaleFactor;
        scale.value = newScale;
        setSize();
        drawCanvas();
    }
};
const zoomToFit = () => {
    const container = wrapper.value.getBoundingClientRect();
    const scaleX = container.width / currentImage.value.width;
    const scaleY = container.height / currentImage.value.height;
    scale.value = Math.min(scaleX, scaleY);
    offsetX.value = 0;
    offsetY.value = 0;
    setSize();
    drawCanvas();
};

// 图片切换
const handleImageSwitch = async (targetIndex) => {
    if (imageList.value.length === 0) {
        ElMessage.warning('图片列表为空，无法切换图片');
        return;
    }

    // 检查是否有未保存的更改
    checkChanged();
    if (canvasChanged.value) {
        showSaveAlert.value = true;
        return;
    }

    // 计算目标图片索引
    const length = imageList.value.length;
    currentImageIndex.value = (targetIndex + length) % length;
    const currentImageItem = imageList.value[currentImageIndex.value];

    if (!currentImageItem || !currentImageItem.imageId) {
        ElMessage.error('无效的图片项');
        return;
    }

    // 更新当前图片ID
    imageId.value = currentImageItem.imageId;

    try {
        // 加载新图片
        await loadImage();
    } catch (error) {
        console.error('加载图片失败:', error);
        ElMessage.error('加载图片失败: ' + error.message);
    }
};

// 执行切换
const executeSwitch = async () => {
    canvasChanged.value = false;

    // 重置画布状态
    rects.value = [];
    polygons.value = [];
    scale.value = 0;
    offsetX.value = 0;
    offsetY.value = 0;

    const length = imageList.value.length;
    if (length === 0) {
        ElMessage.warning('图片列表为空');
        return;
    }

    // 计算目标图片索引
    currentImageIndex.value = (targetImageIndex.value + length) % length;
    const currentImageItem = imageList.value[currentImageIndex.value];

    if (!currentImageItem || !currentImageItem.imageId) {
        ElMessage.error('无效的图片项');
        return;
    }

    // 更新当前图片 ID
    imageId.value = currentImageItem.imageId;

    try {
        // 加载新图片
        await loadImage();
    } catch (error) {
        console.error('加载图片失败:', error);
        ElMessage.error('加载图片失败: ' + error.message);
    }
};

// 判断画布状态是否发生变化
const checkChanged = () => {
    for (let i = 0; i < rects.value.length; i++) {
        if (rects.value[i].changed) {
            canvasChanged.value = true;
            break;
        }
    }
    for (let i = 0; i < polygons.value.length; i++) {
        if (polygons.value[i].changed) {
            canvasChanged.value = true;
            break;
        }
    }
};

// 标签所在行单元格样式
const rectCellClass = ({ row, column, rowIndex, columnIndex }) => {
    row.index = rowIndex;
    column.index = columnIndex;
    if (row instanceof Rect && selectedRectIndex.value === rowIndex) {
        return 'cell-clicked';
    } else if (!(row instanceof Rect) && selectedPolygonIndex.value === rowIndex) {
        return 'cell-clicked';
    }
    return '';
};

// 点击标签所在行
const rectCellClick = (row, column, cell, event) => {
    if (row instanceof Rect) {
        selectedRectIndex.value = row.index;
        selectedRect.value = row;
    } else {
        selectedPolygonIndex.value = row.index;
        selectedPolygon.value = row;
    }
    drawCanvas();
};

// 图片路径单元格格样式
const imageCellClass = ({ row, column, rowIndex, columnIndex }) => {
    row.index = rowIndex;
    column.index = columnIndex;
    if (currentImageIndex.value === rowIndex) {
        return 'cell-clicked';
    }
    return '';
};

// 点击图片路径单元格
const imageCellClick = async (row) => {
    if (!row || !row.imageId) {
        ElMessage.warning('无效的图片数据');
        return;
    }

    // 检查是否有未保存的更改
    checkChanged();
    if (canvasChanged.value) {
        showSaveAlert.value = true;
        return;
    }

    // 更新当前图片 ID
    imageId.value = row.imageId;

    // 清空当前标注
    rects.value = [];
    polygons.value = [];

    // 重置画布状态
    scale.value = 0;
    offsetX.value = 0;
    offsetY.value = 0;

    try {
        // 加载新图片
        await loadImage();

        // 更新当前图片索引
        currentImageIndex.value = imageList.value.findIndex(img => img.imageId === imageId.value);

        // 可选：按需加载标注（取消自动加载，由用户手动触发）
        // await loadAnnotations();
    } catch (error) {
        console.error('切换图片失败:', error);
        ElMessage.error('切换图片失败: ' + error.message);
    }
};

// 撤销多边形点
const undoPolygonPoint = () => {
    if (creatingPolygon.value && currentPolygon.value.length > 0) {
        currentPolygon.value.pop();
        const ctx = canvas.value.getContext('2d');
        const bufferCtx = bufferCanvas.value.getContext('2d');
        const width = canvas.value.width;
        const height = canvas.value.height;
        bufferCtx.clearRect(0, 0, width, height);
        ctx.clearRect(0, 0, width, height);
        drawCanvas();
    }
};


onUnmounted(() => {
    isMounted.value = false; // 标记组件已卸载
    if (currentImage.value) {
        currentImage.value.onload = null; // 取消图片加载回调
    }
    canvas.value = null;
    wrapper.value = null;
});
const currentUserId = ref(4);

// 获取标签列表
const fetchLabels = async () => {
    try {
        const res = await axios.get(`http://localhost:8080/label/list?userId=${currentUserId.value}`); // 使用代理路径
        labels.value = res.data.data;
    } catch (error) {
        ElMessage.error('获取标签失败: ' + error.message);
    }
};

// 获取图像列表（右侧显示）
const fetchImageList = async () => {
    try {
        const res = await axios.get('/api/image/list');
        images.value = res.data.data;
        imageList.value = res.data.data; // 确保两个变量数据一致

        if (images.value.length === 0) {
            ElMessage.warning('图片列表为空，请检查数据源');
        } else {
            // 如果有图片，设置默认显示第一张
            if (!imageId.value) {
                imageId.value = images.value[0].imageId;
                loadImage();
            }
        }
    } catch (error) {
        ElMessage.error('获取图像列表失败: ' + error.message);
    }
};



//查看标注
const viewLabelAnnotations = async (labelId) => {
    if (!labelId) {
        ElMessage.warning('无效的标签ID');
        return;
    }

    try {
        // 获取该标签关联的图片列表
        const res = await axios.get(`/api/annotation/getImageIdsByLabelId?labelId=${labelId}`);

        if (res.data.code !== 200 || !res.data.data || res.data.data.length === 0) {
            ElMessage.warning('该标签没有关联的图片');
            return;
        }

        // 获取第一个关联图片ID
        const firstImageId = res.data.data[0];

        // 检查是否需要保存当前更改
        checkChanged();
        if (canvasChanged.value) {
            showSaveAlert.value = true;
            return;
        }

        // 更新当前图片ID
        imageId.value = firstImageId;

        // 清空当前标注
        rects.value = [];
        polygons.value = [];

        // 重置画布状态
        scale.value = 0;
        offsetX.value = 0;
        offsetY.value = 0;

        try {
            // 加载新图片
            await loadImage();

            // 更新当前图片索引
            currentImageIndex.value = imageList.value.findIndex(img => img.imageId === firstImageId);

            // 加载指定标签的标注
            await loadLabelAnnotations(labelId);
        } catch (error) {
            console.error('加载图片或标注失败:', error);
            ElMessage.error('加载图片或标注失败: ' + error.message);
        }
    } catch (error) {
        console.error('获取标签关联图片失败:', error);
        if (error.response && error.response.status === 404) {
            ElMessage.error('系统未提供查看标注功能，请联系管理员');
        } else {
            ElMessage.error('获取标签关联图片失败: ' + error.message);
        }
    }
};

// 按需加载标注数据
const loadAnnotations = async () => {
    try {
        const res = await axios.get(`/api/annotation/list?imageId=${imageId.value}`);

        if (res.data.code !== 200) {
            console.error('获取标注失败:', res.data.msg);
            ElMessage.warning('获取标注数据失败，可能没有标注');
            return;
        }

        // 清空现有标注
        rects.value = [];
        polygons.value = [];

        // 解析并添加标注
        res.data.data.forEach(annotation => {
            if (annotation.annotationType === 'rect') {
                const [[minX, minY], [maxX, maxY]] = JSON.parse(annotation.coordinates);
                rects.value.push({
                    minX, minY, maxX, maxY,
                    labelId: annotation.labelId,
                    name: labels.value.find(l => l.labelId === annotation.labelId)?.labelName
                });
            } else if (annotation.annotationType === 'polygon') {
                const points = JSON.parse(annotation.coordinates).map(([x, y]) => ({ x, y }));
                polygons.value.push({
                    points,
                    labelId: annotation.labelId,
                    name: labels.value.find(l => l.labelId === annotation.labelId)?.labelName
                });
            }
        });

        // 重绘画布
        drawCanvas();
    } catch (error) {
        console.error('请求标注数据失败:', error);
        ElMessage.error('请求标注数据失败: ' + error.message);
    }
};
// 加载指定标签的标注
const loadLabelAnnotations = async (labelId) => {
    try {
        if (!imageId.value || !labelId) {
            ElMessage.warning('无效的图片ID或标签ID');
            return;
        }

        console.log('加载标注: imageId=', imageId.value, 'labelId=', labelId); // 调试日志

        const res = await axios.get('/annotation/list', {
            params: {
                imageId: imageId.value,
                labelId: labelId
            }
        });

        if (res.data.code !== 200 || !res.data.data) {
            ElMessage.warning('没有找到该标签的标注数据');
            return;
        }

        // 清空当前标注
        rects.value = [];
        polygons.value = [];

        // 处理返回的标注数据
        processAnnotations(res.data.data);
    } catch (error) {
        console.error('获取标签标注失败:', error);
        ElMessage.error('获取标签标注失败: ' + error.message);
    }
};

// 处理标注数据
const processAnnotations = (annotations) => {
    try {
        if (!annotations || annotations.length === 0) {
            return;
        }

        annotations.forEach(annotation => {
            const { type, points, labelName, color } = annotation;

            if (type === 'rect') {
                // 处理矩形标注
                if (points && points.length === 4) {
                    rects.value.push({
                        x: points[0],
                        y: points[1],
                        width: points[2] - points[0],
                        height: points[3] - points[1],
                        label: labelName,
                        color: color || '#FF0000'
                    });
                }
            } else if (type === 'polygon') {
                // 处理多边形标注
                if (points && points.length >= 6 && points.length % 2 === 0) {
                    const polygonPoints = [];
                    for (let i = 0; i < points.length; i += 2) {
                        polygonPoints.push({
                            x: points[i],
                            y: points[i + 1]
                        });
                    }

                    polygons.value.push({
                        points: polygonPoints,
                        label: labelName,
                        color: color || '#00FF00'
                    });
                }
            }
        });

        // 重新绘制画布
        drawCanvas();
    } catch (error) {
        console.error('处理标注数据失败:', error);
        ElMessage.error('处理标注数据失败: ' + error.message);
    }
};
</script>

<style scoped>
canvas {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    touch-action: none;
    user-select: none;
    image-rendering: crisp-edges;
}

.container {
    display: flex;
    height: 100%;
    /* 关键修改 */
}

.left,
.right {
    height: 100%;
    /* 继承父容器高度 */
    flex: 20%;
    padding: 1vw;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
}

.left {
    flex: 80%;
    /* 左侧占 80%，右侧占 20%，可根据需求调整 */

}

.left-top {
    flex: 90%;
    /* 按比例分配高度，不再固定 94vh */
    margin-top: 3vh;
    display: flex;
    flex-direction: column;

    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    position: relative;
    overflow: hidden;
}

.left-bottom {
    margin-top: 1vh;
    padding: 1vh;
    display: flex;
    justify-content: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}

.right>.label-list,
.right>.image-list {
    flex: 50%;
    overflow-y: auto;
    margin-bottom: 10px;
}

.cell-clicked {
    background: #fdf5e6 !important;
}

.mouse-position {
    position: absolute;
    bottom: 10px;
    left: 10px;
    background-color: rgba(255, 255, 255, 0.8);
    padding: 5px;
    border-radius: 5px;
    z-index: 1;
}
</style>