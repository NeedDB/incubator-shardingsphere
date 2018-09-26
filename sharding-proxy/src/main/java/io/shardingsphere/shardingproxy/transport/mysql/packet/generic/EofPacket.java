/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.shardingproxy.transport.mysql.packet.generic;

import com.google.common.base.Preconditions;
import io.shardingsphere.shardingproxy.transport.mysql.constant.StatusFlag;
import io.shardingsphere.shardingproxy.transport.mysql.packet.MySQLPacket;
import io.shardingsphere.shardingproxy.transport.mysql.packet.MySQLPacketPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * EOF packet protocol.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/packet-EOF_Packet.html">EOF Packet</a>
 * 
 * @author zhangliang
 * @author wangkai
 */
@RequiredArgsConstructor
@Getter
public final class EofPacket implements MySQLPacket {
    
    /**
     * Header of EOF packet.
     */
    public static final int HEADER = 0xfe;
    
    private final int sequenceId;
    
    private final int warnings;
    
    private final int statusFlags;
    
    public EofPacket(final int sequenceId) {
        this(sequenceId, 0, StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue());
    }
    
    public EofPacket(final MySQLPacketPayload payload) {
        sequenceId = payload.readInt1();
        Preconditions.checkArgument(HEADER == payload.readInt1());
        warnings = payload.readInt2();
        statusFlags = payload.readInt2();
    }
    
    @Override
    public void write(final MySQLPacketPayload payload) {
        payload.writeInt1(HEADER);
        payload.writeInt2(warnings);
        payload.writeInt2(statusFlags);
    }
}