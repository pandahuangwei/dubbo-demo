package com.simple.common.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 18:43.
 */
public abstract class ReloadableSpringBean extends SpringBean {
    private static Collection<ReloadableSpringBean> allReloadableBeans = Collections.synchronizedCollection(new ArrayList<ReloadableSpringBean>());

    /**
     * 重载时间
     */
    private int reloadMinutes = 5;

    /**
     * 重载时间
     */
    private int reloadSeconds = 0;

    /**
     * 是否只执行一次
     */
    private boolean runOnce = false;

    /**
     * 是否转为普通BEAN
     */
    private boolean generalBean = false;

    // 是否执行过
    private boolean loaded = false;

    public boolean isGeneralBean() {
        return generalBean;
    }

    public void setGeneralBean(boolean generalBean) {
        this.generalBean = generalBean;
    }

    public void setRunOnce(boolean runOnce) {
        this.runOnce = runOnce;
    }

    public boolean isRunOnce() {
        return runOnce;
    }

    public void setReloadMinutes(int reloadMinutes) {
        this.reloadMinutes = reloadMinutes;
    }

    public void setReloadSeconds(int reloadSeconds) {
        this.reloadSeconds = reloadSeconds;
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        allReloadableBeans.add(this);

        if (generalBean) {
            return;
        }

        if (reloadMinutes <= 0) {
            throw new RuntimeException(String.format("reloadMinutes must bigger than zero, now is %1$d.", reloadMinutes));
        }

        if (reloadSeconds < 0) {
            throw new RuntimeException(String.format("reloadSeconds must bigger or equals than zero, now is %1$d.", reloadSeconds));
        }

        // 是ReloadThread线程真正在定时执行任务
        ReloadThread t = new ReloadThread(getBeanName() + " reload thread");
        t._reloadOnce();
        t.start();
    }

    public void destroy() {
        super.destroy();
    }

    /**
     * 是否基础资源Bean，其它工作线程需要等待全部的基础资源加载完成<br>
     * 基础资源bean需要重载该方法，return true
     *
     * @return
     */
    protected boolean isBaseResBean() {
        return false;
    }

    /**
     * 等待全部的基础资源Bean初始化完成
     */
    public static void waitInitialize() {
        boolean allInitialize = false;

		/*ThreadUtils.sleep(10 * 1000);

		while (!allInitialize) {
			try {
				allInitialize = true;
				for (ReloadableSpringBean bean : allReloadableBeans) {
					if (bean.isBaseResBean() && (!bean.loaded)) {
						allInitialize = false;
					}
				}

				ThreadUtils.sleep(10 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
    }

    /**
     * 重新加载线程
     */
    class ReloadThread extends Thread {

        public ReloadThread(String name) {
            super(name);
        }

        public void run() {
            if (!isBaseResBean()) {
                waitInitialize();
                // 等待store全部初始化完成
                //StoreReg.waitInitialize();
            }

            if (runOnce) {
                _reloadOnce();
            } else {
                _reload();
            }
        }

        private void _reload() {
            while (true) {
                try {
                    sleep();
                    reload();
                } catch (Exception e) {
                }
            }
        }

        void _reloadOnce() {
            reload();
            loaded = true;
        }

        private void sleep() {
            try {
                if (reloadSeconds != 0) {
                    Thread.sleep(reloadSeconds * 1000L);
                } else {
                    Thread.sleep(reloadMinutes * 1000 * 60L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 由实现类实现定时重载数据方法
     */
    public abstract void reload();

    /**
     * 缓存是否加载完成
     */
    public boolean isLoaded() {
        return loaded;
    }
}
