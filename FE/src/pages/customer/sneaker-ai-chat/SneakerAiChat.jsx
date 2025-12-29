import { useState } from "react";
import { SneakerAiClientApi } from "../../../api/customer/product/SneakerAiClient.api";
import { useNavigate } from "react-router-dom";
import "./sneaker-ai-chat.css";

export default function SneakerAiChat() {
  const [messages, setMessages] = useState([]);
  const [need, setNeed] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const sendMessage = async () => {
    if (!need.trim()) return;

    setMessages((prev) => [...prev, { from: "user", text: need }]);
    setNeed("");
    setLoading(true);

    try {
      const res = await SneakerAiClientApi.consultSneaker(need);

      const data = res?.data?.data;

      // ✅ Nếu BE trả LIST sneaker
      if (Array.isArray(data) && data.length > 0) {
        setMessages((prev) => [...prev, { from: "ai", products: data }]);
      }
      // ✅ Nếu BE trả TEXT (OpenAI fallback)
      else if (typeof data === "string") {
        setMessages((prev) => [...prev, { from: "ai", text: data }]);
      }
      // ✅ Không có gì trả về
      else {
        setMessages((prev) => [
          ...prev,
          { from: "ai", text: "🤖 Mình chưa tìm được sản phẩm phù hợp." },
        ]);
      }
    } catch (e) {
      setMessages((prev) => [
        ...prev,
        { from: "ai", text: "AI đang bận, vui lòng thử lại 😢" },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ai-chat">
      <div className="ai-chat-header">👟 Tư vấn giày Sneaker</div>

      <div className="ai-chat-body">
        {messages.map((m, i) =>
          m.from === "user" ? (
            <div key={i} className="msg user">
              {m.text}
            </div>
          ) : (
            <div key={i} className="msg ai">
              {m.products
                ? m.products.map((p, idx) => (
                    <div key={idx} className="product-card">
                      <b>{p.productName}</b>
                      <div>💰 {p.price?.toLocaleString()} đ</div>
                      <small>{p.reason}</small>
                      <button
                        onClick={() =>
                          navigate(`/detail-product/${p.productId}`)
                        }
                      >
                        Xem chi tiết
                      </button>
                    </div>
                  ))
                : m.text}
            </div>
          )
        )}

        {loading && <div className="msg ai">🤖 Đang tư vấn...</div>}
      </div>

      <div className="ai-chat-input">
        <input
          value={need}
          onChange={(e) => setNeed(e.target.value)}
          placeholder="VD: Giày đi học, êm, dưới 2 triệu"
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button onClick={sendMessage}>Gửi</button>
      </div>
    </div>
  );
}
