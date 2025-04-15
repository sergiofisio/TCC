import {
  format,
  getDate,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  addMonths,
  subMonths,
} from "date-fns";
import { ptBR } from "date-fns/locale";
import { useState } from "react";

export default function UserEmotionDiary({ user }) {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const getMonthData = () => {
    const startMonth = startOfMonth(currentMonth);
    const endMonth = endOfMonth(currentMonth);
    const startDate = startOfWeek(startMonth);
    const endDate = endOfWeek(endMonth);

    const days = [];
    let date = new Date(startDate);

    while (date <= endDate) {
      days.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }

    return days;
  };

  const daysInMonth = getMonthData();

  const emotionsByDate = (user?.todays_user || []).reduce((acc, item) => {
    const dateKey = format(new Date(item.created_at), "yyyy-MM-dd");
    if (!acc[dateKey]) acc[dateKey] = [];
    acc[dateKey].push(item);
    return acc;
  }, {});

  return (
    <>
      <div className="calendar-controls">
        <button onClick={() => setCurrentMonth(subMonths(currentMonth, 1))}>
          {"<"}
        </button>
        <h4>{format(currentMonth, "MMMM yyyy", { locale: ptBR })}</h4>
        <button onClick={() => setCurrentMonth(addMonths(currentMonth, 1))}>
          {">"}
        </button>
      </div>

      <div className="calendar-grid">
        {["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"].map((day, idx) => (
          <div key={idx} className="calendar-header">
            {day}
          </div>
        ))}

        {daysInMonth.map((date, idx) => {
          const dateKey = format(date, "yyyy-MM-dd");
          const emotionEntries = emotionsByDate[dateKey] || [];
          const isOutsideMonth = date.getMonth() !== currentMonth.getMonth();

          return (
            <div
              key={idx}
              className={`calendar-cell ${isOutsideMonth ? "outside" : ""}`}
            >
              <div className="calendar-date">{getDate(date)}</div>
              {[...emotionEntries]
                .sort((a, b) => {
                  const ordem = { manhã: 0, tarde: 1, noite: 2 };
                  return (
                    ordem[a.morning_afternoon_evening] -
                    ordem[b.morning_afternoon_evening]
                  );
                })
                .map((entry, i) => (
                  <div key={i} className="emotion-entry">
                    <span>{entry.morning_afternoon_evening}</span>:{" "}
                    <strong>{entry.emotion_today}</strong>
                  </div>
                ))}
            </div>
          );
        })}
      </div>
    </>
  );
}
