<template>
  <div class="tree-background">
    <!-- 树容器 -->
    <div class="trees-container">
      <div
        class="tree"
        v-for="(tree, index) in trees"
        :key="index"
        :style="{
          left: `${tree.left}%`,
          bottom: `${tree.bottom}%`,
          width: `${tree.width}px`,
          height: `${tree.height}px`,
          animationDelay: `${tree.delay}s`
        }"
      >
        <!-- 树干 -->
        <div class="tree-trunk"></div>
        <!-- 树叶 -->
        <div class="tree-leaves"></div>
      </div>
    </div>

    <!-- 氧气泡泡 -->
    <div
      class="oxygen-bubble"
      v-for="(bubble, index) in oxygenBubbles"
      :key="index"
      :style="{
        left: `${bubble.left}px`,
        top: `${bubble.top}px`,
        opacity: bubble.opacity,
        animationDuration: `${bubble.duration}s`,
        animationDelay: `${bubble.delay}s`,
        width: `${bubble.size}px`,
        height: `${bubble.size}px`,
      }"
      @animationend="removeBubble(index)"
    ></div>
  </div>
</template>

<script>
export default {
  name: 'TreeBackground',
  data() {
    return {
      // 树的配置
      trees: [],
      // 氧气泡泡
      oxygenBubbles: [],
      // 泡泡生成定时器
      bubbleTimer: null
    }
  },
  mounted() {
    // 初始化树
    this.initTrees();

    // 定期生成氧气泡泡，覆盖整个页面
    this.startProducingOxygen();

    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    // 清除定时器
    if (this.bubbleTimer) {
      clearInterval(this.bubbleTimer);
    }
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    // 初始化树
    initTrees() {
      const treeCount = 8 + Math.floor(Math.random() * 5); // 8-12棵树

      for (let i = 0; i < treeCount; i++) {
        this.trees.push({
          left: Math.random() * 100, // 随机水平位置
          bottom: 0, // 底部对齐
          width: 30 + Math.random() * 40, // 树宽30-70px
          height: 150 + Math.random() * 100, // 树高150-250px
          delay: Math.random() * 3 // 随机动画延迟
        });
      }
    },

    // 开始生成氧气泡泡，全页面分布
    startProducingOxygen() {
      // 更短的间隔，生成更多泡泡
      this.bubbleTimer = setInterval(() => {
        // 随机决定泡泡是从树生成还是页面任意位置生成
        if (Math.random() > 0.3) { // 70%概率从树生成
          const treeIndex = Math.floor(Math.random() * this.trees.length);
          this.createOxygenBubbleFromTree(treeIndex);
        } else { // 30%概率从页面任意位置生成
          this.createOxygenBubbleRandom();
        }
      }, 100); // 每100ms生成一个，增加密度
    },

    // 从树上生成泡泡
    createOxygenBubbleFromTree(treeIndex) {
      const tree = this.trees[treeIndex];
      const treeLeft = (tree.left / 100) * window.innerWidth;

      this.oxygenBubbles.push({
        left: treeLeft + (tree.width / 2) - 10,
        top: window.innerHeight - tree.height * 0.8, // 从树的中上部
        opacity: 0.3 + Math.random() * 0.4, // 0.3-0.7的透明度
        duration: 10 + Math.random() * 10, // 10-20秒
        delay: 0,
        size: 8 + Math.random() * 25 // 8-33px大小
      });

      this.limitBubbleCount();
    },

    // 在页面任意位置生成泡泡
    createOxygenBubbleRandom() {
      this.oxygenBubbles.push({
        left: Math.random() * window.innerWidth, // 全页面水平随机
        top: window.innerHeight + 50, // 从页面底部外进入
        opacity: 0.3 + Math.random() * 0.4,
        duration: 15 + Math.random() * 15, // 15-30秒，更慢
        delay: 0,
        size: 8 + Math.random() * 25
      });

      this.limitBubbleCount();
    },

    // 限制泡泡数量
    limitBubbleCount() {
      // 增加最大泡泡数量，确保全页面覆盖
      if (this.oxygenBubbles.length > 300) {
        this.oxygenBubbles.shift();
      }
    },

    // 移除动画结束的泡泡
    removeBubble(index) {
      this.oxygenBubbles.splice(index, 1);
    },

    // 窗口大小变化时重新计算位置
    handleResize() {
      // 调整现有泡泡位置以适应新窗口大小
      this.oxygenBubbles.forEach(bubble => {
        // 保持相对位置比例
        bubble.left = Math.min(bubble.left, window.innerWidth - bubble.size);
      });
    }
  }
}
</script>

<style scoped>
.tree-background {
  position: fixed; /* 改为fixed，确保滚动时背景不动 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1; /* 确保在背景图之上，内容之下 */
  pointer-events: none; /* 让鼠标事件穿透背景 */
}

/* 树容器 */
.trees-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 300px;
  pointer-events: none;
}

.tree {
  position: absolute;
  transform-origin: bottom center;
  animation: sway 10s ease-in-out infinite;
}

.tree-trunk {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 20%;
  height: 45%;
  background-color: #6b3b11;
  border-radius: 5px 5px 0 0;
}

.tree-leaves {
  position: absolute;
  bottom: 35%;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  height: 75%;
  background-color: #1a7d1a;
  border-radius: 50%;
  box-shadow: 0 0 30px rgba(40, 180, 40, 0.5);
}

@keyframes sway {
  0%, 100% { transform: rotate(-5deg); }
  50% { transform: rotate(5deg); }
}

/* 氧气泡泡样式 - 更明显的效果 */
.oxygen-bubble {
  position: absolute;
  background-color: rgba(144, 238, 144, 0.8); /* 更亮的浅绿色 */
  border-radius: 50%;
  pointer-events: none;
  box-shadow: 0 0 20px rgba(144, 238, 144, 0.9); /* 更强的发光效果 */
  animation: float 15s ease-in-out forwards;
}

/* 泡泡漂浮动画 - 覆盖整个页面 */
@keyframes float {
  0% {
    transform: translate(0, 0) scale(1);
    opacity: 0.5;
  }
  25% {
    transform: translate(25px, -200px) scale(1.2);
    opacity: 0.7;
  }
  50% {
    transform: translate(-20px, -400px) scale(1.3);
    opacity: 0.6;
  }
  75% {
    transform: translate(30px, -600px) scale(1.2);
    opacity: 0.4;
  }
  100% {
    transform: translate(-15px, -800px) scale(1);
    opacity: 0;
  }
}
</style>

