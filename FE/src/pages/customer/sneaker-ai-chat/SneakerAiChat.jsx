import { useState } from "react";
import { SneakerAiClientApi } from "../../../api/customer/product/SneakerAiClient.api";
import { Link, useNavigate } from "react-router-dom";
import "./sneaker-ai-chat.css";
import { RobotOutlined, CloseOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

export default function SneakerAiChat() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [need, setNeed] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const sendMessage = async () => {
    if (!need.trim()) return;

    // user message
    setMessages((prev) => [...prev, { from: "user", text: need }]);
    setNeed("");
    setLoading(true);

    try {
      const res = await SneakerAiClientApi.consultSneaker(need);
      const data = res?.data?.data; // ResponseObject.data

      if (data) {
        setMessages((prev) => [
          ...prev,
          {
            from: "ai",
            text: data.message,
            products: data.products || [],
          },
        ]);
      } else {
        setMessages((prev) => [
          ...prev,
          { from: "ai", text: "🤖 Mình chưa hiểu rõ nhu cầu của bạn." },
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
    <div className="ai-chat-wrapper">
      {!open && (
        <div className="ai-chat-button" onClick={() => setOpen(true)}>
          <RobotOutlined />
        </div>
      )}

      {open && (
        <div className="ai-chat">
          <div className="ai-chat-header">
            <span>AI tư vấn</span>
            <CloseOutlined
              className="close-btn"
              onClick={() => setOpen(false)}
            />
          </div>

          <div className="ai-chat-body">
            {messages.map((m, i) =>
              m.from === "user" ? (
                <div key={i} className="msg user">
                  {m.text}
                </div>
              ) : (
                <div key={i} className="msg ai">
                  {/* message */}
                  {m.text && <div className="ai-text">{m.text}</div>}

                  {/* product cards */}
                  {Array.isArray(m.products) &&
                    m.products.length > 0 &&
                    m.products.map((p, idx) => (
                      <div key={idx} className="product-card">
                        <div className="product-name">{p.nameProduct}</div>
                        <div className="product-price">
                          💰 {p.price?.toLocaleString()} đ
                        </div>
                        <div className="product-reason">{p.reason}</div>
                        <Link
                          to={`/detail-product/${p.idProductDetail}`}
                          className="ai-product-detail-link"
                        >
                          Xem chi tiết
                        </Link>
                      </div>
                    ))}
                </div>
              ),
            )}

            {loading && <div className="msg ai typing">🤖 Đang tư vấn...</div>}
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
      )}
    </div>
  );
}
