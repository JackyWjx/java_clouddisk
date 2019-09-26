# vue-manage-system #
<a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.10-brightgreen.svg" alt="vue">
  </a>
  <a href="https://github.com/ElemeFE/element">
    <img src="https://img.shields.io/badge/element--ui-2.8.2-brightgreen.svg" alt="element-ui">
  </a>
  <a href="https://github.com/lin-xin/vue-manage-system/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/mashape/apistatus.svg" alt="license">
  </a>
  <a href="https://github.com/lin-xin/vue-manage-system/releases">
    <img src="https://img.shields.io/github/release/lin-xin/vue-manage-system.svg" alt="GitHub release">
  </a>
  <a href="http://blog.gdfengshuo.com/example/work/#/donate">
    <img src="https://img.shields.io/badge/%24-donate-ff69b4.svg" alt="donate">
  </a>

����Vue.js + Element UI �ĺ�̨����ϵͳ���������[���ϵ�ַ](http://blog.gdfengshuo.com/example/work/)

����Ŀ����vue-cli3�����������vue-cli2��������[�ɰ汾V3.2.0](https://github.com/lin-xin/vue-manage-system/releases/tag/V3.2.0)

> React + Ant Design �İ汾���ڿ����У��ֿ��ַ��[react-manage-system](https://github.com/lin-xin/react-manage-system)

[English document](https://github.com/lin-xin/manage-system/blob/master/README_EN.md)

## ��Ŀ��ͼ ##
### ��¼
![Image text](https://github.com/lin-xin/manage-system/raw/master/screenshots/wms3.png)

### Ĭ��Ƥ�� ###
![Image text](https://github.com/lin-xin/manage-system/raw/master/screenshots/wms1.png)

### ǳ��ɫƤ�� ###
![Image text](https://github.com/lin-xin/manage-system/raw/master/screenshots/wms2.png)

## ����
�����ߺȱ����Ȱɣ�(΢�źţ�linxin_20)

![΢��ɨһɨ](http://blog.gdfengshuo.com/images/weixin.jpg)

## ǰ�� ##
֮ǰ�ڹ�˾����Vue + Element��������˸���̨����ϵͳ�������ܶ��������ֱ�����������ģ�����Ҳ��һЩ�����޷����㡣��ͼƬ�ü��ϴ������ı��༭����ͼ�����Щ�ں�̨����ϵͳ�кܳ����Ĺ��ܣ�����Ҫ�������������������ɡ���Ѱ���������ʹ������Ĺ����У������˺ܶ����⣬Ҳ�����˱���ľ��顣�����ҾͰѿ��������̨����ϵͳ�ľ��飬�ܽ�������̨����ϵͳ���������

�÷�����Ϊһ�׶๦�ܵĺ�̨���ģ�壬�����ھ��󲿷ֵĺ�̨����ϵͳ��Web Management System������������vue.js,ʹ��vue-cli@3.2.3���ּܿ���������ĿĿ¼������Element UI����⣬���㿪�����ټ��ÿ��������������ɫ��ʽ��֧���ֶ��л�����ɫ�����Һܷ���ʹ���Զ�������ɫ��
���Ѿ������� vue-cli@3.2.3�������������

## ���� ##
- [x] Element UI
- [x] ��¼/ע��
- [x] Dashboard
- [x] ���
- [x] Tabѡ�
- [x] ��
- [x] ͼ�� :bar_chart:
- [x] ���ı��༭��
- [x] markdown�༭��
- [x] ͼƬ��ק/�ü��ϴ�
- [x] ֧���л�����ɫ :sparkles:
- [x] �б���ק����
- [x] Ȩ�޲���
- [x] 404 / 403
- [x] �����˵�
- [x] �Զ���ͼ��
- [x] ����ק����
- [x] ���ʻ�

## ��װ���� ##
```
git clone https://github.com/lin-xin/vue-manage-system.git      // ��ģ�����ص�����
cd vue-manage-system    // ����ģ��Ŀ¼
npm install         // ��װ��Ŀ�������ȴ���װ���֮�󣬰�װʧ�ܿ��� cnpm �� yarn

// ��������������������� http://localhost:8080
npm run serve

// ִ�й���������ɵ�dist�ļ��з��ڷ������¼��ɷ���
npm run build
```
## ���ʹ��˵������ʾ ##

### vue-schart ###
vue.js��װsChart.js��ͼ����������ʵ�ַ��[vue-schart](https://github.com/linxin/vue-schart)
<p><a href="https://www.npmjs.com/package/vue-schart"><img src="https://img.shields.io/npm/dm/vue-schart.svg" alt="Downloads"></a></p>

```html
<template>
    <div>
        <schart  class="wrapper"
				:canvasId="canvasId"
				:type="type"
				:data="data"
				:options="options"
		></schart>
    </div>
</template>
	
<script>
    import Schart from 'vue-schart';        // ����Schart���
    export default {
        data: function(){
            return {
                canvasId: 'myCanvas',       // canvas��id
                type: 'bar',                // ͼ������
                data: [
                    {name: '2014', value: 1342},
                    {name: '2015', value: 2123},
                    {name: '2016', value: 1654},
                    {name: '2017', value: 1795},
                ],
                options: {                  // ͼ���ѡ����
                    title: 'Total sales of stores in recent years'
                }
            }
        },
        components: {
            Schart
        }
    }
</script>
<style>
.wrapper{
	width: 7rem;
	height: 5rem;
}
</style>
```

## ����ע������ ##
### һ������Ҳ����õ������ĳЩ����أ�������ô��ģ����ɾ������Ӱ�쵽���������أ� ###

�ٸ����ӣ��Ҳ����� Vue-Quill-Editor ��������������Ҫ���Ĳ��ߡ�

��һ����ɾ���������·�ɣ���Ŀ¼ src/router/index.js �У��ҵ�����������·�ɣ�ɾ��������δ��롣

```JavaScript
{
    // ���ı��༭�����
    path: '/editor',
    component: resolve => require(['../components/page/VueEditor.vue'], resolve) 
},
```

�ڶ�����ɾ�������������ļ�����Ŀ¼ src/components/page/ ɾ�� VueEditor.vue �ļ���

��������ɾ����ҳ�����ڡ���Ŀ¼ src/components/common/Sidebar.vue �У��ҵ�����ڣ�ɾ��������δ��롣
	
```js
{
	index: 'editor',
	title: '���ı��༭��'
},
```

���Ĳ���ж�ظ������ִ���������
	
	npm un vue-quill-editor -S

��ɡ�

### ��������л�����ɫ�أ� ###

��һ������ src/main.js �ļ����ҵ����� element ��ʽ�ĵط�������ǳ��ɫ���⡣

```javascript
import 'element-ui/lib/theme-default/index.css';    // Ĭ������
// import '../static/css/theme-green/index.css';       // ǳ��ɫ����
```

�ڶ������� src/App.vue �ļ����ҵ� style ��ǩ������ʽ�ĵط����л���ǳ��ɫ���⡣

```javascript
@import "../static/css/main.css";
@import "../static/css/color-dark.css";     /*��ɫ����*/
/*@import "../static/css/theme-green/color-green.css";   !*ǳ��ɫ����*!*/
```

���������� src/components/common/Sidebar.vue �ļ����ҵ� el-menu ��ǩ���� background-color/text-color/active-text-color ����ȥ�����ɡ�

## License

[MIT](https://github.com/lin-xin/vue-manage-system/blob/master/LICENSE)