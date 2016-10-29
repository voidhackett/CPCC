# SCUCrawler

川大系列网站通知爬虫

## Support

目前做了教务处、学工部、青春川大

# Construction

SCUCrawler 使用 Web-Magic 作为爬虫框架

## Dependencies

1. webmagic-core
2. webmagic-extension
3. markdownj-core
4. RxJava
5. AVOScloud

## Struction

主要分为 task、page、db 三个部分，每一个独立的 Site 作为一个 task, 爬虫会根据该 Site 指定信息爬数据。每个 Site 下的每篇文章都由 Page 处理，并通过 PagePipleline 交给 PageFactory 进一步处理。PageFactory 对数据完成加工后，交给 Task，由 Task 控制 DB 储存。

Task 由 TaskManager 管理，在 main 中进行任务委托，TaskManager 依次运行每个 Task，从 Task 中获取爬虫信息并运行。

PageFactory 用于控制数据筛选及加工，每个 Task 均可注册 Solver 定制加工过程，Solver 调用顺序由注册顺序决定。

DB 部分负责管理 url 及对应内容。

# TODO

需要从内容中提取不规范的附件


