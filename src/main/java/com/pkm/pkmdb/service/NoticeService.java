package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.NoticeDao;
import com.pkm.pkmdb.object.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeDao noticeDao;
    public Notice getNoticeById(int id){
        return noticeDao.getNoticeById(id);
    }
    public List<Notice> getAllNotice(){
        return noticeDao.getAllNotice();
    }
    public List<Notice> searchNotice(String keyword){
        return noticeDao.searchNotice("%"+keyword+"%");
    }
    public Boolean addNotice(Notice object){
        return noticeDao.addNotice(object);
    }
    public Boolean delNotice(int id){
        return noticeDao.delNotice(id);
    }
    public Boolean upNotice(Notice object){
        return noticeDao.upNotice(object);
    }
}
