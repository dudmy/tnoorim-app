var http = require('http'),		// http 모듈 사용
	io = require('socket.io'),	// socket.io 모듈 사용
	mysql = require("mysql"),	// mysql 모듈 사용
	gcm = require('node-gcm');	// node-gcm 모듈 사용

var connect = require('connect');							// connect 모듈 사용
var app = connect().use(connect.static(__dirname+'/img'));	// static file serve를 위해 경로 설정	
var server = http.createServer(app);						// 웹 서버 생성 및 포트번호 지정
io = io.listen(server);

// mysql 연결을 위한 conn 객체 생성
var conn = mysql.createConnection({
	"host": "localhost",		// ip
	"port": 3306,				// port
	"user": "?",				// 계정
	"password": "?",			// 비밀번호
	"database": "?"		// 스키마명
});

// socket.io 셋팅
io.configure(function () {
	io.set('log level', 2);
});

// 연결 후 소켓
io.sockets.on('connection', function (socket) {

	/*
	 * 각종 목록 불러오는 쿼리 ****************************************************************************************************************************
	 */

	// 카페 목록
	/* data: 구 */
	socket.on('list_cafe', function (data) {
		var sql = "SELECT cafe_name, cafe_branch, time, cafe_tel, cafe_img FROM cafe WHERE address LIKE '% "+data+" %' ";
		conn.query(sql, function (err, rows) {
			if (typeof rows[0] != "undefined") {
				for (var i=0; i<rows.length; i++) {
					socket.emit("list_cafe", rows[i].cafe_name, rows[i].cafe_branch, rows[i].time, rows[i].cafe_tel, rows[i].cafe_img);
				}
			}
		});
	});

	// 기타 내용 삭제...



	/*
	 * 주문 및 결제 과정 쿼리 ****************************************************************************************************************************
	 */

	// 결제 (모바일)
	/* data1: 회원 번호, data2: 카페명, data3: 쿠폰 개수 */
	socket.on('buy_by_mobile', function (data1, data2, data3) {
		var sql = "UPDATE buy SET is_pay=1 WHERE phone='"+data1+"' AND is_pay=0 ";
		conn.query(sql, function (err, result) {
			console.log("카페: "+data2+"  |  번호: "+data1+"  결제 완료(모바일)");
		});
		if (data3 == 0) {
			sql = "INSERT INTO coupon VALUES('"+ data1 +"', '"+ data2 +"', 1)";
			conn.query(sql, function (err, result) {});
		} else {
			sql = "UPDATE coupon SET num = num + 1 WHERE phone='"+data1+"' AND cafe_name='"+data2+"' AND num<10";
			conn.query(sql, function (err, result) {});
		}
	});

	//벗어난 고객 지우기
	/* data1: 회원 번호, data2: 비콘 주소*/
	socket.on('delete_beacon', function (data1, data2) {
		var sql = "DELETE FROM visitor WHERE phone='"+data1+"' AND bd_address='"+data2+"'";
		console.log("카페: " + data2 + "  |  번호: " + data1 + "  방문 삭제");
		conn.query(sql, function (err, result) {});
	});

	// 기타 내용 삭제...



	/*
	 * 푸시 쿼리 ****************************************************************************************************************************
	 */

	// 비콘 이벤트
	/* data1: 비콘 주소, data2: 회원 번호, data3: regid */
	socket.on('beacon_event', function (data1, data2, data) {

		var sql = "SELECT cafe_name, cafe_branch FROM beacon WHERE bd_address='"+data1+"' AND bd_address NOT IN(SELECT DISTINCT bd_address FROM visitor WHERE phone='"+data2+"' AND bd_address='"+data1+"')";
		conn.query(sql, function (err, rows) {
			if (typeof rows[0] != "undefined") {

				var sql_event = "SELECT event_name, event_info FROM event WHERE cafe_name = '"+rows[0].cafe_name+"' AND endday >= date_format(now(), '%Y.%m.%d')";
				conn.query(sql_event, function (err, rows_event) {
					if (typeof rows_event[0] != "undefined") {

						var sql_lover = "SELECT menu_name, price, count(menu_name) AS cnt FROM buy WHERE phone='"+data2+"' AND cafe_name = '"+rows[0].cafe_name+"' GROUP BY menu_name ORDER BY cnt DESC LIMIT 1";
						conn.query(sql_lover, function (err, rows_lover) {
							var my_menu = "메뉴 없음";

							if(typeof rows_lover[0] != "undefined") {
                        		my_menu = rows_lover[0].menu_name;
                        		var sql = "INSERT INTO buy VALUES(0, '"+data2+"', '"+rows[0].cafe_name+"', '"+rows[0].cafe_branch+"', '"+my_menu+"', "+rows_lover[0].price/2+", now() , 'regural', 0)";
                        		conn.query(sql, function (err, result) {});
                    		}

							var message = new gcm.Message({
							    collapseKey: data1+data2,
							    delayWhileIdle: false,
							    timeToLive: 1800,
							    data: {
							    	message: rows_event[0].event_info,
							    	title: "[" + rows[0].cafe_name + "] " + rows_event[0].event_name,
							       	cafe_name: rows[0].cafe_name,
							       	menu_name: my_menu
							   	}
							});
							var sender = new gcm.Sender('?');
							var registrationIds = [];
							registrationIds.push(data);
							sender.send(message, registrationIds, 4, function (err, result) {
								console.log(result);
							});
						});

					}
				});

				var sql_insert = "INSERT INTO visitor VALUES('"+data2+"', '"+data1+"')";
				conn.query(sql_insert, function (err, result) {
					console.log("카페: " + data1 + "  |  번호: " + data2 + "  방문 추가");
				});
			}
		});
	});

	// 기기 ID 저장
	/* data1: 회원 번호, data2: 이벤트 번호 */
	socket.on('set_regid', function (data1, data2) {
		var sql = "INSERT INTO member VALUES('"+data1+"', '"+data2+"' )";
		conn.query(sql, function (err, result) {});
	})
	
});

server.listen(80);	// 80번 포트 사용