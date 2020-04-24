# bbs
This project is an ordinary SpringBoot BBS project. Use SpringMVC+Thymeleaf+MyBatis,quoted QQ login. Code had notes.
Database is used MySQL. There are 3 tables, one for posts, one for user information and one for user permissions.

此次项目是个普通SpringBoot的论坛项目。使用SpringMVC+Thymeleaf+MyBatis。引用了QQ互联。代码里有相应的注释。
数据库使用的是mysql5，有3个表，一个用来放帖子，一个用来放用户信息，还有个用来放置用户权限。
bbs.sql是所用到的数据库。
Controller下的论坛的页面，上传，用户的编辑，和管理员的置顶、删除、加精，取消置顶，取消加精的操作
LoginController是登录，注册，与验证码验证展示、验证的操作。
MesgImgController是发帖时，临时图片存储的位置。
MesgReplyController是回复页面的显示，回复，删除回复等关于单个帖子回复的操作。
MessageController是关于主贴的加精，置顶，删除，显示页面等，关于主贴的操作。
QQController是关于QQ互联，使用QQ登录的操作。
UserPhotoController是关于用户上传头像的操作。
UserProfileController是用户更改个人信息主页的一些信息，比如昵称，性别，密码。后续可以加入手机绑定等操作。

suController的是管理员操作，可以查看当前所有用户信息，并更改用户信息，权限。
UserController是管理员查看所有用户界面的操作。
UserSetController是管理员更改用户信息、权限的操作。

