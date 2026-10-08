package com.example.demo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * AI 宠物咨询助手
 * 基于 Spring AI 框架接入大模型（OpenAI 兼容协议）。
 * 未配置 API Key 时自动降级为本地智能回答（支持健康咨询 + 多样化表达 + 简单上下文）。
 */
@RestController
@RequestMapping("/ai")
@CrossOrigin
public class AIController {

    /** Spring AI 对话客户端 */
    @Resource
    private ChatClient chatClient;

    /**
     * AI 对话接口：POST /ai/chat
     * {"question":"狗狗一直吐怎么办？", "history":[{"role":"user","content":"..."},{"role":"ai","content":"..."}]}
     */
    @PostMapping("/chat")
    @SuppressWarnings("unchecked")
    public String chat(@RequestBody Map<String, Object> body) {
        String question = body.get("question") == null ? "" : body.get("question").toString().trim();
        if (question.isEmpty()) {
            return "请输入您的问题";
        }
        List<Map<String, String>> history = (List<Map<String, String>>) body.get("history");
        try {
            // 有 API Key：把历史对话和问题一起发给大模型
            String prompt = buildPrompt(history, question);
            String answer = chatClient.prompt(prompt).call().content();
            return answer == null || answer.isBlank() ? localAnswer(question, history) : answer;
        } catch (Exception e) {
            // 无 API Key 或网络异常：本地智能回答
            return localAnswer(question, history);
        }
    }

    /** 拼接历史对话 + 当前问题，作为大模型 Prompt */
    private String buildPrompt(List<Map<String, String>> history, String question) {
        StringBuilder sb = new StringBuilder("你是宠物之家的 AI 客服助手，请用亲切简短的中文回答顾客问题。\n");
        if (history != null) {
            for (Map<String, String> m : history) {
                String role = "user".equals(m.get("role")) ? "顾客" : "助手";
                sb.append(role).append("：").append(m.get("content")).append("\n");
            }
        }
        sb.append("顾客：").append(question);
        return sb.toString();
    }

    /**
     * 本地智能回答（无 Key 兜底）：
     * 1) 关键词覆盖：寄养/洗澡/剪毛/驱虫/疫苗/健康/营业/会员/价格
     * 2) 同一类问题多套回答，随机选择，避免每次一样
     * 3) 支持简单上下文：结合上一轮提问回答（如追问"那多少钱"）
     */
    private String localAnswer(String q, List<Map<String, String>> history) {
        // 上下文：取用户上一轮的问题，辅助理解"这个/它/多少"这类追问
        String prevQ = "";
        if (history != null && !history.isEmpty()) {
            for (int i = history.size() - 1; i >= 0; i--) {
                Map<String, String> m = history.get(i);
                if ("user".equals(m.get("role"))) {
                    prevQ = m.get("content") == null ? "" : m.get("content");
                    break;
                }
            }
        }
        String full = prevQ + " " + q;   // 合并上下文与当前问题一起匹配

        // 当前问题优先匹配；追问（如"那多少钱"）单独匹配不到时，才结合上一轮上下文
        String[] tpls = matchAnswer(q);
        if (tpls == null) {
            tpls = matchAnswer(full);
        }
        if (tpls == null) {
            tpls = new String[]{
                "我是宠物之家的 AI 助手 🐶，可以问我洗澡、剪毛、寄养、驱虫、疫苗、会员积分、营业时间、价格等问题，也可以到店咨询哦！",
                "这个我暂时还没学会呢😅 您可以问问宠物寄养、洗护美容、驱虫疫苗、会员积分这些，或者直接到店咨询更方便~"
            };
        }
        // 随机选一套回答，避免每次一样
        return tpls[ThreadLocalRandom.current().nextInt(tpls.length)];
    }

    /** 关键词匹配，返回候选回答；未命中返回 null */
    private String[] matchAnswer(String text) {
        if (text.contains("寄养")) {
            return new String[]{
                "您好！本店提供宠物寄养服务，按天收费，包含喂食、遛宠和每日健康检查，具体价格可到店咨询或查看服务项目哦 🐾",
                "寄养服务是按天收费的～包含喂食、遛宠和健康检查，您想寄养几天可以提前电话预约，我们会照顾好宝贝的！",
                "可以寄养哒！按天计费，含喂食遛宠+健康检查，寄养前建议带宝贝来店熟悉一下环境~"
            };
        } else if (text.contains("洗澡") || text.contains("洗护")) {
            return new String[]{
                "本店宠物洗澡服务 88 元/次，时长约 60 分钟，使用专业宠物香波，洗完还会帮宝贝吹干造型~",
                "洗澡是 88 元一次，全程大约 1 小时，用的专业宠物香波，洗完会吹干做造型，非常舒服哦！"
            };
        } else if (text.contains("剪毛") || text.contains("美容")) {
            return new String[]{
                "剪毛造型服务 120 元/次，时长约 90 分钟，剪完会拍照给主人确认效果~",
                "美容剪毛 120 元，一般 90 分钟左右，可以按您喜欢的造型来修剪！"
            };
        } else if (text.contains("驱虫")) {
            return new String[]{
                "本店提供体内外驱虫服务，65 元/次，约 30 分钟完成，建议每 1-3 个月做一次哦~",
                "驱虫 65 元一次，半小时左右就好，狗狗猫咪都可以做，按时驱虫对宝贝健康很重要！"
            };
        } else if (text.contains("疫苗") || text.contains("打针")) {
            return new String[]{
                "疫苗接种建议到正规宠物医院进行，本店可以帮您推荐附近的医院并协助预约哦~",
                "疫苗这块我们建议去宠物医院接种更稳妥，需要的话我可以帮您登记预约~"
            };
        } else if (text.contains("吐") || text.contains("拉稀") || text.contains("腹泻") || text.contains("不吃") || text.contains("没精神") || text.contains("生病") || text.contains("感冒") || text.contains("软便")) {
            return new String[]{
                "别着急！如果宝贝呕吐/拉稀，先暂停喂食 4-6 小时观察，确保有充足饮水；如果持续呕吐、精神差或带血，请尽快带去医院检查哦。需要的话我们也可以帮忙联系附近的宠物医院 🐾",
                "先别担心～观察一下宝贝的精神和食欲，少量多次喂水；如果呕吐超过 1 天、拉稀带血或者精神萎靡，一定要及时就医，身体健康最重要！"
            };
        } else if (text.contains("营业") || text.contains("几点") || text.contains("时间")) {
            return new String[]{
                "本店营业时间为每天 9:00 - 18:00，节假日安排请查看系统公告哦~",
                "我们每天 9 点到 18 点营业，节假日可能有调整，以系统公告为准～"
            };
        } else if (text.contains("会员") || text.contains("积分")) {
            return new String[]{
                "本店会员消费 1 元积 1 分，50 积分可抵 1 元现金使用，支付时在账单页勾选积分抵扣即可~",
                "会员积分：消费 1 元=1 积分，50 积分抵 1 元，攒多了支付的时候直接抵现金用！"
            };
        } else if (text.contains("价格") || text.contains("多少钱") || text.contains("收费") || text.contains("便宜")) {
            return new String[]{
                "我们店洗澡 88 元、剪毛 120 元、驱虫 65 元，具体项目可以在「服务项目」页面查看，会员还有积分抵现哦~",
                "价格可以参考服务项目页面哦：洗澡 88、剪毛 120、驱虫 65，都是明码标价的~"
            };
        }
        return null;
    }
}
